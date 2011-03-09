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
package ar.com.leak.iolsucker.model.impl.common.login;

import ar.com.leak.iolsucker.model.LoginInfo;


/**
 * @author Juan F. Codagnone
 * @since Mar 2, 2005
 */
public class ParameterLoginInfo implements LoginInfo {
    /** the username...*/
    private final String pass;
    /** the password...*/
    private final String user;

    /**
     * Crea el ParameterLoginInfo.
     *
     * @param user username
     * @param pass username's password
     */
    public ParameterLoginInfo(final String user, final String pass) {
        this.pass = pass;
        this.user = user;
    }
    
    /** @see ar.com.leak.iolsucker.model.LoginInfo#getUsername() */
    public final String getUsername() {
        return user;
    }

    /** @see ar.com.leak.iolsucker.model.LoginInfo#getPassword() */
    public final String getPassword() {
        return pass;
    }

}
