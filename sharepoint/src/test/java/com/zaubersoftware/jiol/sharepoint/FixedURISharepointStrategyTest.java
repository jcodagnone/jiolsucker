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

import static org.junit.Assert.assertEquals;

import java.net.URI;

import org.junit.Test;

import com.microsoft.schemas.sharepoint.soap.Authentication;


/**
 * TODO Descripcion de la clase. Los comenterios van en castellano.
 * 
 * 
 * @author Juan F. Codagnone
 * @since Mar 8, 2011
 */
public class FixedURISharepointStrategyTest {

    /** */
    @Test
    public final void resolv() {
        final URISharepointStrategy s = new FixedURISharepointStrategy(
                URI.create("http://iol2.itba.edu.ar:27521/grado/72.27/"));
        final URI uri = s.getUriForService(Authentication.class);
        assertEquals("http://iol2.itba.edu.ar:27521/grado/72.27/_vti_bin/Authentication.asmx?wsdl",
                uri.toString());
        
    }
}
