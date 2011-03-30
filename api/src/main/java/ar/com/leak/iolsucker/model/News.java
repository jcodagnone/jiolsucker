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
/*
 * Creada el Apr 26, 2005
 */
package ar.com.leak.iolsucker.model;


/**
 * Representa una noticia de IOL
 * 
 * @author Juan F. Codagnone
 * @since Apr 26, 2005
 */
public interface News {
    /**
     * @return el título de la noticia
     */
    String getTitle();
    
    /**
     * @return el contenido de la noticia
     */
    String getBody();
    
    /**
     * marca como leida a la noticia
     *
     */
    void markAsReaded();
}
