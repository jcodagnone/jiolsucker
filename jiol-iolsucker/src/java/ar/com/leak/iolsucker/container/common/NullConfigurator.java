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
package ar.com.leak.iolsucker.container.common;

import java.util.List;

import ar.com.leak.iolsucker.container.Configurator;


/**
 * Implementación tonta de <code>Configurator</code>
 * 
 * @author Juan F. Codagnone
 * @since Sep 6, 2005
 * @see ar.com.leak.iolsucker.container.Configurator
 */
public class NullConfigurator implements Configurator {

    /** @see Configurator#filterControllers(java.util.List) */
    public final List<Runnable> filterControllers(
            final List<Runnable> controllers) {
        
        return controllers;
    }

}
