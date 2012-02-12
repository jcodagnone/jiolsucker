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
package com.zaubersoftware.jiol.sharepoint;

import java.io.InputStream;
import java.net.URI;

import org.apache.commons.io.IOUtils;

import ar.com.leak.iolsucker.model.Course;
import ar.com.leak.iolsucker.model.IolDAO;
import ar.com.leak.iolsucker.model.Material;
import ar.com.leak.iolsucker.model.impl.common.login.ParameterLoginInfo;

/*
 *  Copyright 2011 Juan F. Codagnone <juam at users dot sourceforge dot net>
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

/**
 * TODO Descripcion de la clase. Los comenterios van en castellano.
 * 
 * 
 * @author Juan F. Codagnone
 * @since Mar 1, 2011
 */
public final class SharepointFacadeDriver {

    /** utitlity class */
    private SharepointFacadeDriver() {
        // void
    }

    private static final String KEY_USERNAME = "jiol.username";
    private static final String KEY_PASSWORD = "jiol.password";

    /** main */
    public static void main(final String[] args) throws Exception {
        final String username = System.getProperty(KEY_USERNAME);
        final String password = System.getProperty(KEY_PASSWORD);

        if (null == username) {
            throw new IllegalStateException(
                    "Falta definir la propiedad del sistema " + KEY_USERNAME);
        }
        if (null == password) {
            throw new IllegalStateException(
                    "Falta definir la propiedad del sistema " + KEY_PASSWORD);
        }

        final IolDAO dao = new SharepointIolDAO(new ParameterLoginInfo(username, password), 
                new FixedURISharepointStrategy(URI.create("http://iol2.itba.edu.ar/")));
        for(final Course course : dao.getUserCourses()) {
            System.out.println(course.getName());
            for(final Material material : course.getFiles()) {
                System.out.println(" " + material.getName());
                InputStream is = null;
                try {
                    is = material.getInputStream();
                    IOUtils.toByteArray(is);
                } finally {
                    is.close();
                }
                
            }
        }
    }
}
