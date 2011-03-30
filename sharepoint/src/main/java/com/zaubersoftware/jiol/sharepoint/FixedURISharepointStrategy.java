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

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.ws.Service;

import org.apache.commons.lang.Validate;

/**
 * Implementación que supone cierta estructura de URIs basado
 * en lo que se ve en el sitio.
 *
 * @author Juan F. Codagnone
 * @since Mar 8, 2011
 */
public class FixedURISharepointStrategy implements URISharepointStrategy {

    private static final  String VTI = "_vti_bin/";
    private final URI prefix;
    
    
    /**
     * Creates the FixedURISharepointStrategy.
     *
     */
    public FixedURISharepointStrategy(final URI prefix) {
        Validate.notNull(prefix);
        
        this.prefix = prefix;
    }

    /** Creates the FixedURISharepointStrategy.  */
   public FixedURISharepointStrategy(final URL prefix) throws URISyntaxException {
       this(prefix.toURI());
   }
    
    @Override
    public final URI getUriForService(final Class<? extends Service> service) {
        final StringBuilder sb = new StringBuilder();
        final String uri = prefix.toString();
        sb.append(uri);
        if(!uri.endsWith("/")) {
            sb.append("/");
        }
        sb.append(VTI);
        sb.append(getName(service));
        sb.append(".asmx?wsdl");
        return URI.create(sb.toString());
    }
    
    /** @return the endpoint name */
    private String getName(final Class<? extends Service> service) {
        return service.getSimpleName();
    }
}
