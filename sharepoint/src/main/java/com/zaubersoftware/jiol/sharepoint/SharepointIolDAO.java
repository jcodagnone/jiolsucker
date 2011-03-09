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
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.UnhandledException;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.leak.iolsucker.model.Course;
import ar.com.leak.iolsucker.model.IolDAO;
import ar.com.leak.iolsucker.model.LoginInfo;
import ar.com.leak.iolsucker.model.News;

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
    private final URL url;
    
    /**
     * Creates the SharepointIolDAO.
     */
    public SharepointIolDAO(final LoginInfo login,
            final URL url) throws MalformedURLException {
        Validate.notNull(login);
        Validate.notNull(url);
        
        this.login = login;
        this.url = url;
    }
    
    @Override
    public final Collection<Course> getUserCourses() {
        final Collection<Course> ret = new ArrayList<Course>();
        
        for(final String materia : Arrays.asList("72.27")) {
            try {
                final SharepointServiceFactory factory = new JAXWSharepointServiceFactory(
                        new FixedURISharepointStrategy(url.toURI()),
                                login, materia);
                ret.add(new SharepointCourse(materia, materia, factory));
            } catch (final MalformedURLException e) {
                throw new UnhandledException(e);
            } catch (final URISyntaxException e) {
                throw new UnhandledException(e);
            }
        }
        
        return ret;
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
