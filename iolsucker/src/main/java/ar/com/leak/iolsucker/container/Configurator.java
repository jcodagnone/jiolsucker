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
package ar.com.leak.iolsucker.container;

import java.util.List;



/**
 * Interfase que deben implementar los configuradores
 *
 * @author Juan F. Codagnone
 * @since Aug 15, 2005
 */
public interface Configurator {
    /**
     * Permite al configurador filtrar (dejar de lado) controladores que existen
     * en el BeanFactory
     * 
     * @param controllers lista de controladores a filtrar
     * @return una lista filtrada
     */
    List<Runnable> filterControllers(List<Runnable> controllers);
}
