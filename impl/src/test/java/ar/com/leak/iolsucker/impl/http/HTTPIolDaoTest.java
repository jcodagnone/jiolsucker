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
package ar.com.leak.iolsucker.impl.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import junit.framework.TestCase;
import ar.com.leak.iolsucker.impl.common.RelativeLocationValidator;
import ar.com.leak.iolsucker.impl.common.login.ParameterLoginInfo;
import ar.com.leak.iolsucker.impl.mock.MockHTTPClient;
import ar.com.leak.iolsucker.model.Course;
import ar.com.leak.iolsucker.model.Material;

/**
 * 
 * Testeos de unidad para el IolDao
 * 
 * @author Juan F. Codagnone
 * @since Apr 22, 2005
 * @link ar.com.leak.iolsucker.model.IolDAO
 */
public class HTTPIolDaoTest extends TestCase {

    /**
     * @see HTTPIolDao#getUserCourses()
     */
    public final void testGetUserCourses() throws Exception {
        NamingMapper namingMapper = new NamingMapper(new URL(
                "http://silvestre.itba.edu.ar/itbaV/"));
        final Map<String, URL> map = new HashMap<String, URL>();
        final ClassLoader classLoader = getClass().getClassLoader();
        map.put(namingMapper.getUserCoursesCommand(), MockHTTPClient
                .getResourceURL(classLoader, "html/mynav_courses.html"));
        final HTTPClient hc = new MockHTTPClient(namingMapper, map);

        final HTTPIolDao dao = new HTTPIolDao(new ParameterLoginInfo("foo",
                "bar"), hc, 
                new HTTPMaterialFactory(1,
                        new MockRequestFactory(new RelativeLocationValidator(),
                                new HashMap<String, Collection<Material>>())));

        final Course [] expectedCourses = {
                new HTTPCourse(hc, null, "GRA", "GRADO", 1),
                new HTTPCourse(hc, null, "-12", "CEITBA", 3),
                new HTTPCourse(hc, null, "-21", "OFRECIMIENTO DE PASANTIAS", 3),
                new HTTPCourse(hc, null, "61.31", "DERECHO PARA INGENIEROS", 4),
                new HTTPCourse(hc, null, "21.01", "ELECTROTECNIA I", 4),
                new HTTPCourse(hc, null, "94.22", "FORMACION GENERAL II", 4),
                new HTTPCourse(hc, null, "93.71", 
                        "INVESTIGACION OPERATIVA.", 4),
                new HTTPCourse(hc, null, "93.05", "MATEMATICA V", 4),
                new HTTPCourse(hc, null, "71.19", "SISTEMAS DE COMPUTACION", 4),

        };

        final Collection<Course> tmp = 
            new Vector<Course>(Arrays.asList(expectedCourses));
        final Collection ret = dao.getUserCourses();
        assertEquals(tmp, ret);
    }

    /**
     * @throws Exception on error
     */
    public final void testNews() throws Exception {
        final NamingMapper namingMapper = getNamingMapper();
        String [][] rsc = {
                {
                    namingMapper.getNewsCommand(true), 
                    "html/novlistall_all.html"
                }, {
                    namingMapper.getNewsCommand(false),
                     "html/novlistall_unread.html" 
                }, {
                    namingMapper.getUserCoursesCommand(),
                    "html/mynav_courses.html" 
                },
        };
        HTTPIolDao dao = getDAO(rsc, namingMapper);
        Collection unread = dao.getUnreadNews();
        Collection all = dao.getNews();

        assertEquals(unread.size(), 2);
        final int expectedNumberOfFiles = 51; 
        assertEquals(all.size(), expectedNumberOfFiles);

    }

    // //////////////////////////////////////////////////////////////////////

    /** 
     * @return the naming mapper
     * @throws MalformedURLException error
     */
    private NamingMapper getNamingMapper() throws MalformedURLException {
        return new NamingMapper(new URL("http://silvestre.itba.edu.ar/itbaV/"));
    }

    /**
     * @param resources array {name, path al resource} donde name es parte de 
     *    la url (sin la base), y classpath al archivo que cargar.
     * @param namingMapper namming mapper
     * @return un HTTPIolDao funcional
     * @throws Exception on error
     */
    private HTTPIolDao getDAO(final String [][] resources, 
            final NamingMapper namingMapper)
            throws Exception {
        final Map<String, URL> map = new HashMap<String, URL>();
        final ClassLoader classLoader = getClass().getClassLoader();
        for(int i = 0; i < resources.length; i++) {
            String [] entry = resources[i];
            map.put(entry[0], MockHTTPClient.getResourceURL(classLoader,
                    entry[1]));
        }
        final HTTPClient hc = new MockHTTPClient(namingMapper, map);
        return new HTTPIolDao(new ParameterLoginInfo("foo", "bar"), hc,
                new HTTPMaterialFactory(1, new MockRequestFactory(
                                new RelativeLocationValidator(),
                                new HashMap<String, Collection<Material>>())));
    }
}
