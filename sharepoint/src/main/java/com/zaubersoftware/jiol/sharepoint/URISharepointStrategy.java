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

import java.net.URI;

import javax.xml.ws.Service;

/**
 * Estrategia para resolver URIs de servicios SOAP.
 * 
 * 
 * @author Juan F. Codagnone
 * @since Mar 8, 2011
 */
public interface URISharepointStrategy {
    /** retorna la url de servicio para un servicio de la materia */
    URI getUriForService(Class<? extends Service> service);
}
