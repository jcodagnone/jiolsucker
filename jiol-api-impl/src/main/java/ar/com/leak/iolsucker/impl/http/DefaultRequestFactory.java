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
import java.util.Collection;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.com.leak.iolsucker.impl.common.Validator;
import ar.com.leak.iolsucker.impl.http.util.URIHelper;
import ar.com.leak.iolsucker.model.Material;


/**
 * Default implementation for RequestFactory
 * 
 * @author Juan F. Codagnone
 * @since Aug 21, 2005
 */
public class DefaultRequestFactory implements HTTPRequestFactory {
    /** injected relative path validator */
    private final Validator validator;
    /** fileFactory */
    private final MaterialFileFactory fileFactory;
    
    /**
     * Creates the DefaultRequestFactory.
     *
     * @param relativePathValidator relative path validator
     * @param fileFactory factory que crea las clases que representan a los 
     *        archivos de material didactico
     */
    public DefaultRequestFactory(final Validator relativePathValidator,
            final MaterialFileFactory fileFactory) {
        Validate.notNull(relativePathValidator, "relativePathValidator");
        Validate.notNull(fileFactory, "fileFactory");
        validator = relativePathValidator;
        this.fileFactory = fileFactory;
    }
    
    /**
     * @see HTTPRequestFactory#createRequest(HTTPClient, java.lang.String, 
     *      java.lang.String, java.lang.String, java.util.Collection,
     *       java.util.concurrent.BlockingQueue)
     */
    public Runnable createRequest(final HTTPClient httpClient, 
            final String uri, final String parentUri, final String referer,
            final Collection<Material> ret, 
            final BlockingQueue<Runnable> requests) {
        return new Request(httpClient, uri, parentUri, referer, ret, 
                requests, this);
    }
    
    /**
     *  Factory de archivos de material didactico para este request factory
     *  
     * @author Juan F. Codagnone
     * @since Aug 23, 2005
     */
    public interface MaterialFileFactory {
        /**
         * @param client http client to use
         * @param parent folder address that holds this file
         * @param fid file id
         * @return un nuevo material archivo
         */
        Material createMaterialFile(final HTTPClient client, 
                final String parent, final int fid);
    }
    
    /** 
     * implementación de @link{MaterialFileFactory}
     *  
     * @author Juan F. Codagnone
     * @since Aug 23, 2005
     */
    public static class DefaultMaterialFileFactory 
           implements MaterialFileFactory {
        /** @see MaterialFileFactory#createMaterialFile(HTTPClient, 
         * java.lang.String, int)
         */
        public final Material createMaterialFile(final HTTPClient client, 
                final String parent, final int fid) {
            
            return new HTTPMaterialFile(client, parent, fid);
        }
        
    }
    
    /**
     * A request process
     *
     * @author Juan F. Codagnone
     * @since Aug 19, 2005
     */
    private class Request implements Runnable {
        /** class logger */
        private final Log logger = LogFactory.getLog(Request.class);
        /** the http client*/
        private final HTTPClient httpClient;
        /** the request uri... */
        private final String uri;
        /** the request parent uri... */
        private final String parentUri;
        /** the request referer */
        private final String referer;
        /** collección que contiene los resultados */
        private final Collection<Material> ret;
        /** collección con lo que queda procesar */
        private final BlockingQueue<Runnable> requests;
        /** requestFactory */
        private final HTTPRequestFactory requestFactory;
        
        /**
         * Creates the Request.
         *
         * @param httpClient the httpClient
         * @param uri an uri
         * @param parentUri the parent of the uri
         * @param referer the referer
         * @param ret la colleccion donde se guardan los resultados
         * @param requests cola donde se insertan los requests a procesar
         * @param requestFactory request factory a usar
         */
        public Request(final HTTPClient httpClient, final String uri,
                final String parentUri, final String referer, 
                final Collection<Material> ret, 
                final BlockingQueue<Runnable> requests,
                final HTTPRequestFactory requestFactory) {
            this.httpClient = httpClient;
            this.uri = uri;
            this.parentUri = parentUri;
            this.referer = referer;
            this.ret = ret;
            this.requests = requests;
            this.requestFactory = requestFactory;
        }
        
  
        /**
         * se conecta a la página web y la procesa.
         * encola nuevos directorios a procesar, y agrega a ret los archivos
         * encontrados
         * 
         * @throws IOException on error
         */
        private void work() throws IOException {
            logger.debug("getting material for `" + uri 
                    + "' referer is `" + referer + "'");
            final URLConnection huc = httpClient.getConnection(uri);
            huc.connect();
            final InputStream stream = huc.getInputStream(); 
            final BufferedReader in = new BufferedReader(
                    new InputStreamReader(stream));

            final String html = LinkParser.getString(in);
            in.close();
            Collection<Link> links = LinkParser.getLinks(html);
            
            for(final Link link : links) {
                final String bajarsz = "javascript:BajaArch(";

                if(link.getUri().startsWith(bajarsz)) {
                    String s = link.getUri().substring(bajarsz.length());
                    s = s.substring(0, s.length() - 1);
                    logger.debug("bajar " + s);
                    ret.add(fileFactory.createMaterialFile(httpClient, 
                            parentUri, new Integer(s).intValue()));
                }
            }
            
            links = LinkFolderParser.getLinks(html);
            for(final Link link : links) {
                final String l = link.getUri();
                String path = link.getAnchor();
                final URIHelper uriHelper = new URIHelper(l);
                
                if(path.length() != 0 && uriHelper.getParameters().size() == 1 
                        && uriHelper.hasParam("fid")) {
                    path = (parentUri.length() == 0 ? "" :  parentUri + "/") 
                        + path;
                    if(validator.isValid(path)) {
                        logger.debug("a dir! " + path + "(" + path
                                + ")");
                        requests.add(requestFactory.createRequest(
                                httpClient, l, path, uri, ret, 
                                requests));
                        ret.add(new HTTPMaterialFolder(path));
                    } else {
                        logger.warn("el servidor está pasando un "
                                + "path que parece peligroso: `" 
                                + path + "'. ignorandolo");
                    }
                }
            }
        }

        /**  @see java.lang.Runnable#run() */
        public final void run() {
            try {
                work();
            } catch(final IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

