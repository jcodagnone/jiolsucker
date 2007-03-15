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
package ar.com.leak.iolsucker.container.common;

import java.net.MalformedURLException;

import ar.com.leak.iolsucker.container.Options;
import ar.com.leak.iolsucker.container.OptionsDAO;

/**
 * DAO de opciones que:
 *    al cargar llena con valores dummy la configuracion
 *    y al salvar imprime los valores al stdout
 *  
 * @author Juan F. Codagnone
 * @since Apr 12, 2005
 */
public class MockOptionsDAO implements OptionsDAO {

    /** @see OptionsDAO#getOptions() */
    public final Options getOptions() throws MalformedURLException {
        Options options = new Options();
        
        options.setUsername("123456");
        options.setPassword("top secret!");
        options.setRepository("/tmp/foo");
        options.setSaveData(true);
        options.setSavePassword(false);
        
        return options;
    }

    /** @see OptionsDAO#saveOptions(Options) */
    public final void saveOptions(final Options options) {
        System.out.println(options);
    }

    /** @see OptionsDAO#clearOptions() */
    public final void clearOptions() {
        // void
    }

    
}
