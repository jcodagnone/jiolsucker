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
package ar.com.leak.iolsucker.impl.mock;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import ar.com.leak.iolsucker.impl.http.HTTPClient;
import ar.com.leak.iolsucker.impl.http.NamingMapper;

/**
 * 
 * TODO Agregar comentarios pertinentes
 * 
 * @author Juan F. Codagnone
 * @since Apr 13, 2005
 */
public class MockHTTPClient extends HTTPClient {
    /** */
    private final Map resources;

    /**
     * Crea el MockHTTPClient.
     *
     * @param namingMapper naming mapper a usar
     * @param resources Map<String, URL>, donde String es una direccion que se
     *       espera, y URL es la direccion del contenido a descargar.
     */
    public MockHTTPClient(final NamingMapper namingMapper, 
            final Map resources) {
        super(namingMapper);
        // Logger.getRoot().setPriority(Priority.DEBUG);
        this.resources = resources;
    }

    /** @see HTTPClient#getConnection(String) */
    public final URLConnection getConnection(final String urlsz, 
            final MethodEnum method, final String args) throws IOException {
        URL url = (URL)resources.get(urlsz);

        if(url == null) {
            throw new AssertionError(
                    "no se como simular el pedido a la pagina " + urlsz);
        }

        return url.openConnection();
    }

    /** @see HTTPClient#getConnection(URL, 
     * ar.com.leak.iolsucker.impl.http.HTTPClient.MethodEnum, java.lang.String)
     */
    @Override
    public final URLConnection getConnection(final URL url, 
            final MethodEnum method, final String args) throws IOException {
        throw new UnsupportedOperationException();
    }
    
    /**
     * helper method que retorna la url de un recurso en el classpath
     * 
     * @param loader classloader a usar
     * @param s dirección del recurso
     * @return la url del recurso
     */
    public static final URL getResourceURL(final ClassLoader loader, 
            final String s) {
        URL url = loader.getResource(s);

        if(url == null) {
            throw new AssertionError("no se pudo cargar recurso `" + "" + s
                    + "'");
        }
        return url;
    }
}
