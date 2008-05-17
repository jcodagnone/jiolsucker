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

import java.util.Collection;
import java.util.concurrent.BlockingQueue;

import ar.com.leak.iolsucker.model.Material;


/**
 * Factory que crea requests 
 *
 * @author Juan F. Codagnone
 * @since Aug 21, 2005
 */
public interface HTTPRequestFactory {
    /**
     * Factory method: Creates a request
     *
     * @param httpClient the httpClient
     * @param uri an uri
     * @param parentUri the parent of the uri
     * @param referer the referer
     * @param ret la colleccion donde se guardan los resultados
     * @param requests cola donde se insertan los requests a procesar
     * 
     * @return un nuevo 
     */
    Runnable createRequest(final HTTPClient httpClient, final String uri,
            final String parentUri, final String referer, 
            final Collection<Material> ret, 
            final BlockingQueue<Runnable> requests);
}