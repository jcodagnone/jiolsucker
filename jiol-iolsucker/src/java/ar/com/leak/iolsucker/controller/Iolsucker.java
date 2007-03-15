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

package ar.com.leak.iolsucker.controller;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.com.leak.iolsucker.model.Course;
import ar.com.leak.iolsucker.model.IolDAO;
import ar.com.leak.iolsucker.view.Repository;

/**
 * Main Class. Toca Rock and Roll
 * 
 * @author Juan F. Codagnone
 * @since Feb 25, 2005
 */
public class Iolsucker implements Runnable  {
    /** class logger */
    private final Log logger = LogFactory.getLog(Iolsucker.class);
    /** un predicado que permite filtrar cursos a la otra del download */
    private final Comparator<Course> courseComparator;
    /** un IolDao de donde sacar la informacion (inyectado) */
    private final IolDAO dao;
    /** un repositorio sobre el cual operar (inyectado) */
    private final Repository repository;
    /** Un predicado que permite filtrar cursos a la otra del download */
    private final Predicate coursePredicate;

    /**
     * Crea el Iolsucker.
     *
     * @param repository Un repositorio sobre el cual operar
     * @param dao  Un IolDao de donde sacar la informacion
     * @param coursesComparator Un Comparador que permite determinar un orden 
     *         de cursos 
     * @param coursePredicate  Un predicado que permite filtrar cursos a la 
     *        otra del download
     */
    public Iolsucker(final Repository repository,  final IolDAO dao, 
            final Comparator<Course> coursesComparator, 
            final Predicate coursePredicate) {
        
        if(repository == null || dao == null || coursesComparator == null 
                || coursePredicate == null) {
            throw new NullArgumentException("ningun argumento puede ser nulo");
        }
        
        this.repository = repository;
        this.dao = dao;
        this.courseComparator = coursesComparator;
        this.coursePredicate = coursePredicate;
    }
     
    /** @see java.lang.Runnable#run() */
    public final void run() {
        try {
            final List<Course> courses = new ArrayList<Course>(CollectionUtils.
                    select(dao.getUserCourses(), coursePredicate));
            Collections.sort(courses, courseComparator);
            
            for(final Course course : courses) {
                logger.info(course.getCode() + " (" + course.getName() + ")");
                repository.syncMaterial(course);
            }
        } catch(final Exception e) {
            logger.error("corriendo", e);
        }
    }
}
