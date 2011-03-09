/*
 * Copyright 2005 Juan F. Codagnone <juam at users dot sourceforge dot net>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ar.com.leak.iolsucker.model.impl.common.login;

import ar.com.leak.iolsucker.model.LoginInfo;

/**
 * Obtiene la información de login usando las propiedades del sistema
 * <code>iolsucker.user</code> y <code>iolsucker.pass</code>
 * 
 * @author Juan F. Codagnone
 * @since Mar 1, 2005
 */
public final class SystemPropertyLoginInfo implements LoginInfo {
    /** propiedad del sistema usada para proveer el usuario */
    private static final String USERNAME = "iolsucker.user";
    /** propiedad del sistema usada para proveer la password */
    private static final String PASSWORD = "iolsucker.pass";
    /** el usuario */
    private final String user;
    /** la password */
    private final String pass;

    /** Crea el SystemPropertyLoginInfo. */
    public SystemPropertyLoginInfo() {
        user = System.getProperty(USERNAME);
        if(user == null || user.length() == 0) {
            throw new IllegalArgumentException("debe usar el propiedad "
                    + USERNAME + " para definir el usuario");
        }

        pass = System.getProperty(PASSWORD);
        if(pass == null || pass.length() == 0) {
            throw new IllegalArgumentException("debe usar el propiedad "
                    + PASSWORD + " para definir la password");
        }
    }

    /** @see ar.com.leak.iolsucker.model.LoginInfo#getUsername() */
    public String getUsername() {
        return user;
    }

    /** @see ar.com.leak.iolsucker.model.LoginInfo#getPassword() */
    public String getPassword() {
        return pass;
    }

}
