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
package ar.com.leak.iolsucker.impl.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import ar.com.leak.iolsucker.model.Course;
import ar.com.leak.iolsucker.model.IolDAO;
import ar.com.leak.iolsucker.model.Material;
import ar.com.leak.iolsucker.model.News;
import ar.com.leak.iolsucker.model.impl.common.StringNews;

/**
 * Dao con datos de jugete para poder probar el repositorio y el controlador
 * 
 * @author Juan F. Codagnone
 * @since Mar 1, 2005
 */
public class MockIolDao implements IolDAO {
    /** material didactico */
    private static final Material [][] MATERIAL = {
            {   
                new MockMaterialDir("practica1"),
                new MockMaterialFile("practica1/ej1.txt",
                        "Elvis is alive and designed the Z80000"),
                new MockMaterialFile("practica1/ej2.txt",
                  "When a wiseguy says, \"Pull my finger,\" don't do it. "),
                new MockMaterialDir("practica2"),
                new MockMaterialFile("practica2/ej3.txt",
                        "I have a very small mind and must live with it. "
                        + "-- E. Dijkstra"),
                new MockMaterialFile("practica2/ej4.txt",
                        "Computers are like air conditioners. They stop "
                        + "working when you open Windows."),
                new MockMaterialDir("practica3"), 
            }, {
                    new MockMaterialDir("test1"),
                    new MockMaterialFile("test1/ej4.txt",
                      "Never ever underestimate the power of human stupidity."),
                    new MockMaterialFile("test2/ej5.txt",
                   "Computers are not intelligent.  They only think they are."),
            }
        };

    /** eh */
    private static final Course [] DEFAULT_COURSES = {
            new MockCourse("GRADO", "-1", 2, new ArrayList<Material>()),
            new MockCourse("Corte y Confección", "1.2", 4, Arrays
                    .asList(MATERIAL[0])),
            new MockCourse("Cocina", "2.3", 4, Arrays.asList(MATERIAL[1])) };

    /** DEFAULT_UNREAD_NEWS */
    private static final News [] DEFAULT_UNREAD_NEWS = {
            new StringNews("Doom day",
                    "Chicos!! mañana es el día del juicio final"),
            new StringNews("Bar", "desde hoy, está prohibido fumar en el bar"),
    };

    /** DEFAULT_READ_NEWS */
    private static final News [] DEFAULT_READ_NEWS = {
            new StringNews("Doom day",
                    "Chicos!! mañana es el día del juicio final"),
            new StringNews("Bar", "desde hoy, está prohibido fumar en el bar"), 
    };
    /** noticias no leidas disponibles al dao */
    private final News [] unreadNews;
    /** noticias leidas disponibles al dao */
    private final News [] readNews;
    /** cursos disponibles al dao */
    private final Course [] courses;

    /** @see #MockIolDao(Course[], News[], News[]) */
    public MockIolDao() {
        this(DEFAULT_COURSES, DEFAULT_UNREAD_NEWS, DEFAULT_READ_NEWS);
    }

    /**
     * Crea el MockIolDao.
     *
     * @param courses cursos disponibles al dao
     * @param unreadNews noticias sin leer disponibles al dao
     * @param readNews noticias leidas disponibles al dao
     */
    public MockIolDao(final Course [] courses, final News [] unreadNews, 
            final News [] readNews) {
        this.courses = courses;
        this.unreadNews = unreadNews;
        this.readNews = readNews;
    }

    /** @see IolDAO#getUserCourses() */
    public final Collection<Course> getUserCourses() {
        return Arrays.asList(courses);
    }

    /** @see IolDAO#dispose() */
    public void dispose() throws Exception {
    }

    /** @see IolDAO#getUnreadNews()*/
    public final Collection<News> getUnreadNews() {
        return Arrays.asList(unreadNews);
    }

    /** @see IolDAO#getNews() */
    public final Collection<News> getNews() {
        Collection<News> ret = getUnreadNews();
        ret.addAll(Arrays.asList(readNews));

        return ret;
    }
}
