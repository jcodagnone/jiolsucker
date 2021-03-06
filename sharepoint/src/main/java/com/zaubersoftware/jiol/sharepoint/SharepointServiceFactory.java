/**
 * Copyright (c) 2005-2011 Juan F. Codagnone <http://juan.zaubersoftware.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zaubersoftware.jiol.sharepoint;

import javax.xml.ws.BindingProvider;

import ar.com.zauber.leviathan.api.URIFetcher;

/**
 * Permite obtener endpoints de servicios de sharepoint.
 * 
 * @author Juan F. Codagnone
 * @since Mar 8, 2011
 */
public interface SharepointServiceFactory {

    /** gets the {@link SiteDataSoap} service */
//    SiteDataSoap getSiteDataService();
    
    /** el fetcher para utilizar */
    URIFetcher getUriFetcher();
    
    void configureService(BindingProvider provider);
}
