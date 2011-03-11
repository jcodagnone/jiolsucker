/*
 *  Copyright 2011 Juan F. Codagnone <juam at users dot sourceforge dot net>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.zaubersoftware.jiol.sharepoint;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.lang.UnhandledException;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.leak.iolsucker.model.Course;
import ar.com.leak.iolsucker.model.IolDAO;
import ar.com.leak.iolsucker.model.LoginInfo;
import ar.com.leak.iolsucker.model.News;
import ar.com.zauber.leviathan.common.AbstractAsyncTaskExecutor;

import com.microsoft.schemas.sharepoint.soap.ArrayOfSFPUrl;
import com.microsoft.schemas.sharepoint.soap.ArrayOfSListWithTime;
import com.microsoft.schemas.sharepoint.soap.ArrayOfSWebWithTime;
import com.microsoft.schemas.sharepoint.soap.ArrayOfString;
import com.microsoft.schemas.sharepoint.soap.SWebMetadata;
import com.microsoft.schemas.sharepoint.soap.SWebWithTime;
import com.microsoft.schemas.sharepoint.soap.SiteData;
import com.microsoft.schemas.sharepoint.soap.SiteDataSoap;

/**
 * Implementation agains IOL2 (sharepoint based)
 *
 *
 * @author Juan F. Codagnone
 * @since Mar 8, 2011
 */
public class SharepointIolDAO implements IolDAO {
    private final LoginInfo login;
    private final Logger logger = LoggerFactory.getLogger(SharepointIolDAO.class);
    private final URISharepointStrategy uriStrategy;
    
    /**
     * Creates the SharepointIolDAO. 
     */
    public SharepointIolDAO(final LoginInfo login,
            final URISharepointStrategy uriSharepointStrategy) throws MalformedURLException, URISyntaxException {
        Validate.notNull(login);
        Validate.notNull(uriSharepointStrategy);
        
        this.login = login;
        this.uriStrategy = uriSharepointStrategy;
    }
    
    
    private Collection<Course> courses;
    @Override
    public final Collection<Course> getUserCourses() {
        if(courses == null) {
            courses = Collections.unmodifiableCollection(getUserCoursesReal());
        }
        return courses;
    }
    
    /** real implementation for {@link #getUserCourses()} */
    protected final Collection<Course> getUserCoursesReal() {
        final Collection<Course> ret = new ArrayList<Course>();
        final List<String []> materiasUrlTitle = new ArrayList<String[]>();
        final SharepointServiceFactory serviceFactory = 
            new JAXWSharepointServiceFactory(uriStrategy, login);
        
        final WebListing webs = getWebs(uriStrategy, serviceFactory);
        final TaskExecutor executorService = new TaskExecutor();
        
        
        for(final SWebWithTime web : webs.getWebs()) {
            // paralelizamos la busqueda de materias
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        final WebListing sub = getWebs(new FixedURISharepointStrategy(
                                URI.create(web.getUrl())), serviceFactory);
                        for(final SWebWithTime s : sub.getWebs()) {
                            executorService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    final WebListing subsub = getWebs(new FixedURISharepointStrategy(
                                            URI.create(s.getUrl())), serviceFactory);
                                    final String [] uriTitle = new String[]{s.getUrl(), 
                                            subsub.getMetadata().getTitle()};
                                    materiasUrlTitle.add(uriTitle);
                                }
                            });
                        }
                    } catch(SOAPFaultException t) {
                        // esto puede estar bien...significa 403 probablemente
                    }
                }
            });
        }
        try {
            executorService.awaitIdleness();
        } catch (InterruptedException e) {
            throw new UnhandledException(e);
        }
        
        for(final String []urlTitle : materiasUrlTitle) {
            ret.add(new SharepointCourse(urlTitle[1], URI.create(urlTitle[0]), 
                    serviceFactory));
        }
        
        return ret;
    }

    /** retorna las sub webs de una web */
    private WebListing getWebs(final URISharepointStrategy uriStrategy, 
            final SharepointServiceFactory serviceFactory) {
        SiteDataSoap site;
        try {
            site = new SiteData(uriStrategy.getUriForService(SiteData.class)
                    .toURL()).getSiteDataSoap();
        } catch (MalformedURLException e) {
            throw new UnhandledException(e);
        }
        serviceFactory.configureService((BindingProvider) site);
        final Holder<java.lang.Long> getWebResult = new Holder<Long>();
        final Holder<SWebMetadata> sWebMetadata = new Holder<SWebMetadata>();
        final Holder<ArrayOfSWebWithTime> vWebs = new Holder<ArrayOfSWebWithTime>();
        final Holder<ArrayOfSListWithTime> vLists = new Holder<ArrayOfSListWithTime>();
        final Holder<ArrayOfSFPUrl> vFPUrls = new Holder<ArrayOfSFPUrl>();
        final Holder<java.lang.String> strRoles = new Holder<String>();
        final Holder<ArrayOfString> vRolesUsers = new Holder<ArrayOfString>();
        final Holder<ArrayOfString> vRolesGroups = new Holder<ArrayOfString>();
        site.getWeb(getWebResult, sWebMetadata, vWebs, vLists, vFPUrls, strRoles, vRolesUsers, 
                    vRolesGroups);
        return new WebListing(sWebMetadata.value, vWebs.value.getSWebWithTime());
    }

    @Override
    public final Collection<News> getUnreadNews() {
        logger.error("No está implementado la búsqueda de noticias :(");
        return Collections.EMPTY_LIST;
    }

    @Override
    public final Collection<News> getNews() {
        logger.error("No está implementado la búsqueda de noticias :(");
        return Collections.EMPTY_LIST;
    }

    @Override
    public void dispose() throws Exception {
        // TODO Auto-generated method stub

    }
}

/** weblisting result */
class WebListing {
    private final SWebMetadata metadata;
    private final List<SWebWithTime> webs;

    /**
     * Creates the WebListing.
     *
     */
    public WebListing(final SWebMetadata metadata, final List<SWebWithTime> webs) {
        Validate.notNull(metadata);
        Validate.notNull(webs);
        
        this.metadata = metadata;
        this.webs = webs;
    }

    public final SWebMetadata getMetadata() {
        return metadata;
    }
    
    public final List<SWebWithTime> getWebs() {
        return webs;
    }
}

/**
 * Ejectutador de tareas asincronicas que lleva la cuenta de que se está ejecutando. 
 * 
 * @author Juan F. Codagnone
 * @since Mar 11, 2011
 */
class TaskExecutor extends AbstractAsyncTaskExecutor implements Executor {
    private final ExecutorService executorService = Executors.newFixedThreadPool(20);

    @Override
    public void execute(final Runnable command) {
        incrementActiveJobs();
        
        try {
            executorService.execute(new Runnable() {
                
                @Override
                public void run() {
                    try {
                        command.run();
                    } finally {
                        decrementActiveJobs();
                    }
                    
                }
            });
        } catch(final Exception e){
            // only on error we decrement.
            decrementActiveJobs();
        }
    }
}