/*
 *  Copyright 2008 Juan F. Codagnone <juam at users dot sourceforge dot net>
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
package ar.com.leak.iolsucker;

import java.net.URL;

import junit.framework.TestCase;
import ar.com.leak.iolsucker.impl.common.RelativeLocationValidator;
import ar.com.leak.iolsucker.impl.common.login.SystemPropertyLoginInfo;
import ar.com.leak.iolsucker.impl.http.DefaultRequestFactory;
import ar.com.leak.iolsucker.impl.http.HTTPClient;
import ar.com.leak.iolsucker.impl.http.HTTPIolDao;
import ar.com.leak.iolsucker.impl.http.HTTPMaterialFactory;
import ar.com.leak.iolsucker.impl.http.NamingMapper;
import ar.com.leak.iolsucker.model.IolDAO;

/**
 * Sirve para probar que todavia nos podemos loguear en iol
 * 
 * @author Juan F. Codagnone
 * @since May 26, 2008
 */
public class LoginDriver extends TestCase {

    /** asd */
    public final void testFoo() throws Exception {
        final IolDAO iolDAO = new HTTPIolDao(
                new SystemPropertyLoginInfo(),
                new HTTPClient(new NamingMapper(new URL(
                        "http://silvestre.itba.edu.ar/itbaV/"))),
                new HTTPMaterialFactory(10, new DefaultRequestFactory(
                        new RelativeLocationValidator(),
                        new DefaultRequestFactory.DefaultMaterialFileFactory())));
        
        System.out.println(iolDAO.getUnreadNews());
    }
}
