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
package ar.com.leak.iolsucker.impl.http.util;

import java.util.*;


/**
 * Helper para parsear URIs.
 * 
 * @author Juan F. Codagnone
 * @since Feb 25, 2005
 */
public class URIHelper {
    /** base de la url. ej: http://www.leak.com.ar/servlet */
    private final String base;
    /** Map<String, String> con los parametros. Ej {[action, logout]} */
    private final Map<String, String> map = new HashMap<String, String>();
   
    /**
     * Crea el URIHelper.
     *
     * @param param representancion string de la url
     */
    public URIHelper(final String param) {
        String string = param;
        int i = string.indexOf('?');
        if(i == -1) {
            base = string;
        } else {
            base = string.substring(0, i);
            string = string.substring(i + 1);
            for(i = string.indexOf('&'); i != -1;) {
                String entry = string.substring(0, i);
                int j = entry.indexOf("=");
                assert j != -1;
                String key = entry.substring(0, j);
                String val = entry.length() > j + 1 
                                        ? entry.substring(j + 1) : "";
                map.put(key, val);
                
                if(i + 1 < string.length()) {
                    string = string.substring(i + 1);
                    i = string.indexOf('&');
                } else {
                    i = -1;
                }
            }
            int j = string.indexOf('=');
            if(j != -1) {
                final String key = string.substring(0, j);
                final String val = string.length() > j + 1 
                        ? string.substring(j + 1) : "";
                map.put(key, val);
            }
        }
    }
    
    /**
     * @return Ej. si uri = pepe.asp?a=1&b=2, retornaria pepe.asp
     */
    public final String getBase() {
        return base;
    }

    /**
     * @return mapa con los parametros
     */
    public final Map getParameters() {
        return Collections.unmodifiableMap(map);
    }

    /**
     * @param param parametro
     * @return <code>true</code> si está presente determinado parámetro
     */
    public final boolean hasParam(final String param) {
        return map.containsKey(param);
    }
    
    /**
     * @param param key
     * @return el valor de un parámetro
     * @throws IllegalArgumentException si el parametro no existe
     */
    public final String getParam(final String param) 
       throws IllegalArgumentException {
        String ret = map.get(param);
        if(ret == null) {
            throw new IllegalArgumentException("no existe el parametro " 
                    + param);
        }
        
        return ret;
    }

    /**
     * @param arg a testear
     * @return true si el parámetro está contenida en URIHelper. Ejemplo:
     *         "mynav.asp?cmd=help" está contenido en
     *         "mynav.asp?cmd=help&param1=something" contiene a pero no en
     *         "mynav.asp?param1=something"
     * 
     */
    public final boolean isContainedIn(final URIHelper arg) {
        boolean ret = true;

        if(getBase().equals(arg.getBase())) {
            for(Iterator i = getParameters().keySet().iterator(); ret
                    && i.hasNext();) {
                String key = (String)i.next();
                if(arg.hasParam(key)) {
                    ret &= arg.getParam(key).equals(getParam(key));
                } else {
                    ret = false;
                }
            }
        } else {
            ret = false;
        }

        return ret;
    }

    /** @see Object#toString() */
    public final String toString() {
        final StringBuffer sb = new StringBuffer(getBase());

        if(getParameters().size() != 0) {
            sb.append("?");
        }

        for(Iterator i = getParameters().keySet().iterator(); i.hasNext();) {
            String param = (String)i.next();
            sb.append(param);
            sb.append("=");
            sb.append(getParam(param));
            if(i.hasNext()) {
                sb.append("&");
            }
        }
        return sb.toString();
    }
}
