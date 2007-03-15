/*
 *  Copyright 2005 Juan F. Codagnone <juam at users dot sourceforge dot net>
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
package ar.com.leak.iolsucker.impl.http;

import java.io.*;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.com.leak.iolsucker.impl.common.login.WrongCredentials;
import ar.com.leak.iolsucker.impl.http.util.URIHelper;
import ar.com.leak.iolsucker.model.*;


/**
 * Implentación del Data access Object de iol, usando una conexión http, 
 * emulando una persona.
 * 
 * @author Juan F. Codagnone
 * @since Feb 25, 2005
 * @navasoc - - - ar.com.leak.iolsucker.impl.http.HTTPClient
 * @depend  - crea - ar.com.leak.iolsucker.impl.http.HTTPCourse
 * @depend  - crea - ar.com.leak.iolsucker.impl.http.HTTPClient
 */
public class HTTPIolDao implements IolDAO {
    /** class logger */
    private final Log logger = LogFactory.getLog(HTTPIolDao.class);
    /** injected NamingMapper */
    private final NamingMapper namingMapper;
    /** injected HTTPClient */
    private final HTTPClient httpClient;
    /** injected HTTPMaterialFactory */
    private final HTTPMaterialFactory httpMaterialFactory;

    /**
     * Crea el HTTPIolDao.
     *
     * @param info login information a usar
     * @param httpClient httpclient a usar
     * @param httpMaterialFactory material factoroy
     * @throws Exception on error
     */
    public HTTPIolDao(final LoginInfo info, final HTTPClient httpClient,
            final HTTPMaterialFactory httpMaterialFactory) throws Exception {

        if(info == null || httpClient == null 
                || httpMaterialFactory  == null) {
            throw new NullArgumentException(
                    "info, httpClient, relativePathValidator");
        }

        this.httpMaterialFactory = httpMaterialFactory;
        this.namingMapper = httpClient.getNamingMapper();
        this.httpClient = httpClient;

        if(!login(info.getUsername(), info.getPassword())) {
            throw new WrongCredentials("el login falló, chequee el nombre "
                    + "de usuario o la password");
        }
    }
    
    /** @see ar.com.leak.iolsucker.IolDAO#getUserCourses() */
    public final Collection<Course> getUserCourses() {
        Collection<Course> ret = new ArrayList<Course>();
        
        try {
            URLConnection huc = httpClient.getConnection(namingMapper
                    .getUserCoursesCommand());
            
            logger.debug("obteniendo materias del usuario");
            huc.connect();
            final InputStream in = huc.getInputStream();
            final Collection links = LinkParser.getLinks(new BufferedReader(
                    new InputStreamReader(in)));
            in.close();
            for(Iterator i = links.iterator(); i.hasNext();) {
                Link link = (Link)i.next();
                Course course = createCourse(link);
                logger.debug("course: " + course + " del link: " + link);
                if(course != null) {
                    ret.add(course);
                }
            }
        } catch(Exception e) {
            logger.error("obteniendo las materias del usuario", e);
        }
        return ret;
    }


    /** @see ar.com.leak.iolsucker.model.IolDAO#getUnreadNews() */
    public final Collection<News> getUnreadNews() {
        try {
            return getNews(false);
        } catch(IOException e) {
            // TODO ver como solucionar esto
            throw new RuntimeException(e);
        }
    }

    /** @see ar.com.leak.iolsucker.model.IolDAO#getNews() */
    public final Collection<News> getNews() {
        try {
            return getNews(true);
        } catch(IOException e) {
            // TODO ver como solucionar esto
            throw new RuntimeException(e);
        }
    }
    

    /** @see ar.com.leak.iolsucker.model.IolDAO#dispose() */
    public final void dispose() throws Exception {
        logout();
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * desloguea de IOL si estamos logueados
     * 
     * @throws IOException on error
     */
    private void logout() throws IOException {
        URLConnection huc = httpClient.getConnection(namingMapper
                .getLogoutCommand());

        logger.info("login out");
        huc.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(huc
                .getInputStream()));

        while(in.readLine() != null) {
            // void 
        }
        in.close();
    }

    
    /**
     * inicia una session a IOL. este metodo debe ser llamado antes que 
     * cualquiera
     *  
     * @param user  el DNI con el cual loguearse
     * @param password el password a cambiar
     * 
     * @return true se estamos logueados
     *  
     * @throws IOException si hay algun problema con la red
     */
    private boolean login(final String user, final String password) 
    throws IOException {
        URLConnection huc = null;

        logger.info("login in as user `".concat(user).concat("'"));
        huc = httpClient.postConnection(namingMapper.getLoginCommand(),
                encodeUserAndPassword(user, password));
        BufferedReader in = new BufferedReader(new InputStreamReader(huc
                .getInputStream()));

        return wasLogginSuccessfull(in);
    }
    
    /**
     * verifica que estemos logueados. cuando lo estamos mynav.asp nos devuelve
     * un link con logout
     * @param in pagina web que sale despues de loguearse
     * @return <code>true</code> si el contenido de in indica que estamos 
     *  logueados
     * @throws IOException on error
     */
    private boolean wasLogginSuccessfull(final BufferedReader in) 
      throws IOException {
        Collection c = LinkParser.getLinks(in);
        in.close();
        boolean ret = false;

        for(Iterator i = c.iterator(); i.hasNext();) {
            Object o = i.next();

            if(o instanceof Link) {
                Link link = (Link)o;

                if(link.getUri().startsWith(namingMapper.getLogoutCommand())) {
                    ret = true;
                    break;
                }
            } else {
                throw new AssertionError();
            }
        }

        return ret;
    }

    ////////////////////////////////////////////////////////////////////////////
    /**
     * codifica los caracteres inseguros del user y forma la cadena de
     * parametros usada en el POST
     * 
     * @param user the username
     * @param password the username's password
     * @return un string listo para hacer el login
     */
    private String encodeUserAndPassword(final String user, 
            final String password) {
        StringBuffer sb = new StringBuffer();

        sb.append("txtdni=");
        try {
            sb.append(URLEncoder.encode(user, "US-ASCII"));
            sb.append("&txtpwd=");
            sb.append(URLEncoder.encode(password, "US-ASCII"));
            sb.append("&Submit=Conectar&cmd=login");
        } catch(UnsupportedEncodingException e) {
            throw new AssertionError();
        }

        return sb.toString();
    }

    /**
     * given a course context link, creates a course 
     * @param courseLink change context link
     * @return a course
     */
    private HTTPCourse createCourse(final Link courseLink) {
        HTTPCourse ret = null;
        URIHelper uh = new URIHelper(courseLink.getUri());

        if(namingMapper.getChangeContextCommandBase().isContainedIn(uh)) {
            final String levelsz = "nivel";
            final String codesz = "snivel";

            if(uh.hasParam(levelsz) && uh.hasParam(codesz)) {
                final int level = new Integer(uh.getParam(levelsz)).intValue();
                final String code = uh.getParam(codesz);

                ret = new HTTPCourse(httpClient, httpMaterialFactory, code,
                        courseLink.getAnchor(), level);
                ret.setLink(courseLink.getUri());
            }
        }

        return ret;
    }

    /**
     * @param all <code>true</code> if we want all the news (readed and 
     *        unreaded)
     * @return the news
     * @throws IOException on error
     */
    private Collection<News> getNews(final boolean all) throws IOException {
        final String cmd = namingMapper.getNewsCommand(all);
        final URLConnection huc = httpClient.getConnection(cmd);
        logger.debug("obteniendo noticas del usuario");
        huc.connect();
        final Collection<News> ret = new ArrayList<News>();
        final InputStream in =  huc.getInputStream();
        final Collection links = LinkParser.getLinks(new BufferedReader(
                new InputStreamReader(in)));
        in.close();
        final URIHelper uh = new URIHelper(cmd);
        for(Iterator i = links.iterator(); i.hasNext();) {
            final Link link = (Link)i.next();
            final URIHelper helper = new URIHelper(link.getUri());
            if(helper.getBase().equals(uh.getBase())) {
                if(helper.hasParam("ign")) {
                    ret.add(new HTTPNews(httpClient, link));
                } else {
                    logger.warn("link " + link + "wasn't expected. report.");
                }
            }
        }
        
        return ret;
    }
}
