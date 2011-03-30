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
package ar.com.leak.iolsucker.model;

import java.util.Collection;


/**
 * Data Access Object para un Itba Online
 * 
 * @author Juan F. Codagnone
 * @since Feb 25, 2005
 * @has - - n ar.com.leak.iolsucker.model.Course
 * @has - - n ar.com.leak.iolsucker.model.News 
 */
public interface IolDAO {

    /**
     * @return una coleccion de <code>Course</code> con los cursos a los que 
     *         el usuario está registrado
     */
    Collection <Course> getUserCourses();

    /**
     * @return una colección con las noticias no leidas
     */
    Collection <News> getUnreadNews();
    
    /**
     * 
     * @return una colección con todas las noticias
     */
    Collection <News> getNews();
    
    
    /**
     * Le indica al DAO que puede liberar sus recursos
     * 
     * @throws Exception cuando hay algún error
     */
    void dispose() throws Exception;
}
