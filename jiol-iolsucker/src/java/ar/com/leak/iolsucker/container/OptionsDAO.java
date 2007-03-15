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
package ar.com.leak.iolsucker.container;

/**
 * persiste y borra Options
 * 
 * @author Juan F. Codagnone
 * @since Apr 12, 2005
 */
public interface OptionsDAO {

    /**
     * carga las opciones
     * 
     * @return las opciones
     * @throws Exception on error
     */
    Options getOptions() throws Exception;
    
    /**
     * Guarda las opciones en algun lado, permitiendo poderlas buscar mas tarde
     * 
     * @param options opciones a guardar
     * @throws Exception on error
     */
    void saveOptions(Options options) throws Exception;
    
    /**
     * borra las opciones si es que habia algo guardado
     * @throws Exception on error
     */
    void clearOptions() throws Exception;
}
