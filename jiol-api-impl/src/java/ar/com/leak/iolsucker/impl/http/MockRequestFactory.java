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

import java.util.*;
import java.util.concurrent.BlockingQueue;

import ar.com.leak.iolsucker.impl.common.Validator;
import ar.com.leak.iolsucker.model.Material;



/**
 * TODO Brief description.
 *
 * TODO Detail
 *
 * @author Juan F. Codagnone
 * @since Aug 21, 2005
 */
 public class MockRequestFactory extends DefaultRequestFactory {
    /** mapa de uris */
    private final Map<String, Collection<Material>> expectedURIs;
    /** set de String con todos los pedidos hechos */
    private final Set<String> requested = new HashSet<String>();

    /**
     * Creates the MockHttpRequestFactory.
     *
     * @param relativeValidator validador
     * @param expectedURIs mapa 
     */
    public MockRequestFactory(final Validator relativeValidator, 
            final Map<String, Collection<Material>> expectedURIs) {
        super(relativeValidator, new DefaultMaterialFileFactory());
        this.expectedURIs = expectedURIs;
    }

    
    /**
     * @see HTTPRequestFactory#createRequest(HTTPClient, java.lang.String, 
     * java.lang.String, java.lang.String, java.util.Collection,
     *  java.util.concurrent.BlockingQueue)
     */
    public final Runnable createRequest(final HTTPClient httpClient, 
            final String uri, final String parentUri, final String referer, 
            final Collection<Material> ret, 
            final BlockingQueue<Runnable> requests) {

        if(expectedURIs.containsKey(uri)) {
            requested.add(uri);
            return super.createRequest(httpClient, uri, parentUri, referer,
                    ret, requests);
        } else {
            throw new AssertionError("uri no esperada: `" + uri + "'");
        }
    }

    /**
     * 
     * @return <code>true</code> si todas las uris que se esperaban fueron
     *         visitadas
     */
    public final boolean allURIsWereVisited() {
        return expectedURIs.keySet().equals(requested);
    }
}