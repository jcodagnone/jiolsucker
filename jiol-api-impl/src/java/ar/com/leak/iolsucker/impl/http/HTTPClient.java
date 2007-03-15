/*
 * Copyright 2005 Juan F. Codagnone <juam at users dot sourceforge dot net>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ar.com.leak.iolsucker.impl.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Cliente HTTP. Abstraccion para obtener páginas web con soporte de cookies.
 * 
 * @author Juan F. Codagnone
 * @since Feb 25, 2005
 * @has - - - ar.com.leak.iolsucker.impl.http.NamingMapper
 */
public class HTTPClient {

    /**
     * Typesafe enum for HTTP methods 
     * @author Juan F. Codagnone
     */
    public static final class MethodEnum {
        /** nombre del método */
        private final String name;

        /**
         * Crea el MethodEnum.
         * @param name nombre del método
         */
        private MethodEnum(final String name) {
            this.name = name;
        }

        /** @see Object#toString() */
        public String toString() {
            return name;
        }

        /** HTTP GET method */
        public static final MethodEnum GET = new MethodEnum("GET");
        /** HTTP POST method */
        public static final MethodEnum POST = new MethodEnum("POST");
        /** HTTP HEAD method */
        public static final MethodEnum HEAD = new MethodEnum("HEAD");
    };

    /** logger */
    private final Log logger = LogFactory.getLog(HTTPClient.class);
    /** holds the nammingMapper injected */
    private final NamingMapper namingMapper;
    /** user-agent to show to the web-servers */
    private static final String USER_AGENT = 
        "jiolsucker (VERSION; OperatingSys )";
    /** holds the server session cookie */
    private String sessionCookie = null;

    /**
     * Crea el HTTPClient.
     * @param namingMapper NamingMapper used in this http client
     */
    public HTTPClient(final NamingMapper namingMapper) {
        this.namingMapper = namingMapper;
    }

    /**
     * simplificación de getConnection para el metodo GET
     * @see #getConnection(String, MethodEnum, String)
     */
    public final URLConnection getConnection(final String urlsz) 
      throws IOException {
        return getConnection(urlsz, MethodEnum.GET, null);
    }

    
    /**
     * simplificación de getConnection para el metodo HEAD
     * @see #getConnection(String, MethodEnum, String)
     */
    public final URLConnection headConnection(final String urlsz) throws 
        IOException {
        return getConnection(urlsz, MethodEnum.HEAD, null);
    }

    /**
     * simplificación de getConnection para el metodo POST
     * @see #getConnection(String, MethodEnum, String)
     */
    public final URLConnection postConnection(final String urlsz, 
            final String args)
            throws IOException {
        return getConnection(urlsz, MethodEnum.POST, args);
    }

    /** @see #getConnection(String, MethodEnum, String) */
    public URLConnection getConnection(final URL url, final MethodEnum method,
            final String args) throws IOException {
        HttpURLConnection huc = null;

        try {
            logger.debug(method.toString() + " " + url);
            huc = (HttpURLConnection)url.openConnection();

            huc.setDoInput(true);
            huc.setAllowUserInteraction(true);
            huc.setInstanceFollowRedirects(true);
            huc.setRequestProperty("User-Agent", USER_AGENT);
            huc.setRequestMethod(method.toString());
            if(this.sessionCookie != null) {
                huc.setRequestProperty("Cookie", this.sessionCookie);
            }
            
            if(method == MethodEnum.POST) {
                huc.setDoOutput(true);
                OutputStream os = huc.getOutputStream();
                os.write(args.getBytes("US-ASCII"));
                os.close();
            }
            if(this.sessionCookie == null) {
                String cookie = huc.getHeaderField("Set-Cookie");
                if(cookie == null) {
                    // TODO quejarme
                } else {
                    int index = cookie.indexOf(";");
                    if(index >= 0) {
                        this.sessionCookie = cookie.substring(0, index);
                    } else {
                        // TODO deberia quejarme
                    }
                }
            }
        } catch(MalformedURLException e) {
            throw new IllegalStateException();
        } catch(ProtocolException e) {
            throw new IllegalStateException();
        }

        return huc;
    }
    /**
     * Retorna una conexión.
     * 
     * @param urlsz parte relativa path inicial. ej: novlistAll.asp?pepe=
     * @param method GET, POST, ...
     * @param args argumento para el post
     * @return una conexion URL
     * @throws IOException si hay un error
     */
    public URLConnection getConnection(final String urlsz,
            final MethodEnum method, final String args) throws IOException {
        return getConnection(new URL(namingMapper.getURLBase() + urlsz), method,
                args);
    }

    /**
     * @return the naming mapper
     */
    public final NamingMapper getNamingMapper() {
        return namingMapper;
    }
}
