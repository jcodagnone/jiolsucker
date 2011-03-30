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
package ar.com.leak.iolsucker.view.common;

import java.util.Comparator;

import ar.com.leak.iolsucker.model.Course;


/**
 * Comparador de cursos, que ordena desendetemente por nivel, y ascendentemente
 * por nombre de materia
 * 
 * @author Juan F. Codagnone
 * @since Mar 2, 2005
 * @see ar.com.leak.iolsucker.model.Course
 */
public class CoursesComparator implements Comparator<Course> {

    /** @see Comparator#compare(java.lang.Object, java.lang.Object) */
    public final int compare(final Course c1, final Course c2) {
        int ret;
        if(c1.getLevel() > c2.getLevel()) {
            ret = -1;
        } else if(c1.getLevel() < c2.getLevel()) {
            ret = 1;
        } else {
            ret =  c1.getName().compareTo(c2.getName());
        }
        
        return ret;
    }
}
