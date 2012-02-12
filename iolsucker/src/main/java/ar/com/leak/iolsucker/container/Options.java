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
package ar.com.leak.iolsucker.container;

import java.net.MalformedURLException;
import java.net.URL;

import ar.com.leak.iolsucker.model.LoginInfo;

/**
 * Bean cuyas propiedades son la configuracion usada en la vista swing  
 * 
 * @author Juan F. Codagnone
 * @since Apr 12, 2005
 */
public class Options implements LoginInfo  {
    /** el usuario usado para entrar a iol */
    private String username = "";
    /** la password del usuario */
    private String password = "";
    /** path al repositorio */
    private String repository;
    /** los datos estos persisten en algun lado? */
    private boolean saveData = false;
    /** si <code>saveData</code>, <code>savePassword</code> y 
     * <code>startWithoutPrompting</code> son <code>true también, entonces 
     * comienza el download con la última conf grabada sin preguntar nada */
    private boolean startWithoutPrompting = false;
    /** si persisten...la password tambien persiste? */
    private boolean savePassword = false;
    /** default base url de iol */
    private final String defaultURLBase = "http://iol2.itba.edu.ar/";
    private final String oldURLBase = "http://silvestre.itba.edu.ar/itbaV/";
    
    /** base url de iol */
    private URL uRLBase;
    /** proxy host */
    private String proxyHost = "";
    /** proxy port */
    private int proxyPort = DEFAULT_PROXY_PORT;
    /** puerto de proxy default */
    private static final int DEFAULT_PROXY_PORT = 8080;
    /** filtrar los cursos */ 
    private boolean filterCourses;
    /**
     * Crea el Options.
     *
     * @throws MalformedURLException on error
     */
    public Options() throws MalformedURLException {
        uRLBase = new URL(defaultURLBase);
    }

    /** @see ar.com.leak.iolsucker.model.LoginInfo#getPassword() */
    public final String getPassword() {
        return password;
    }
    
    /** @param password password del usuario para entrar a iol */
    public final void setPassword(final String password) {
        this.password = password;
    }
    
    
    /** @see #setRepository(String) */
    public final String getRepository() {
        return repository;
    }
    
    
    /**
     * @param repository base del repositorio donde se guardan los archivos 
     */
    public final void setRepository(final String repository) {
        this.repository = repository;
    }
    
    
    /** @see #setSaveData(boolean) */
    public final boolean isSaveData() {
        return saveData;
    }
    
    
    /**
     * @param saveData <code>true</code> si se almacena los datos
     */
    public final void setSaveData(final boolean saveData) {
        this.saveData = saveData;
        if(!saveData) {
            setSavePassword(false);
        }
    }
    
    /**@see #setStartWithoutPrompting(boolean) */
    public final boolean isStartWithoutPrompting() {
        return startWithoutPrompting;
    }
    
    /**
     * @param startWithoutPrompting Si es <code>true</code>, cuando 
     * {@link #isSaveData()} y {@link #isSavePassword()} también lo sean,
     * arranca la GUI bajando los archivos sin preguntar nada.
     */
    public final void setStartWithoutPrompting(boolean startWithoutPrompting) {
        this.startWithoutPrompting = startWithoutPrompting;
    }
    
    /**
     * @return <code>true</code> si se almacena la password para no tenerla
     *         que volver a tipear
     */
    public final boolean isSavePassword() {
        return savePassword;
    }
    
    /**
     * @param savePassword <code>true</code> si se almacena la password en el 
     *                algun lado. (para no tenerla que tipear de vuelta)
     */
    public final void setSavePassword(final boolean savePassword) {
        if(saveData) {
            this.savePassword = savePassword;
        } else {
            this.savePassword = false;
        }
    }
    
    
    /** @see LoginInfo#getUsername() */
    public final String getUsername() {
        return username;
    }
    

    /** @param username el username (login de iol) a setear */
    public final void setUsername(final String username) {
        this.username = username;
    }

    /** @return <code>true</code> si este objeto tiene datos consistentes */
    public final boolean isValid() {
        return getUsername().length() != 0 && getPassword().length() != 0
                && getRepository() != null && getRepository().length() != 0;
    }

    /**
     * @return la url base de la webapp de IOL
     */
    public final URL getURLBase() {
        return uRLBase;
    }

    /**
     * @param base la url base (http://silvestre.itba.edu.ar/itbaV/ de IOL a 
     *             setear
     */
    public final void setURLBase(final URL base) {
        if(base.toString().equals(oldURLBase)) {
            try {
                uRLBase = new URL(defaultURLBase);
            } catch (MalformedURLException e) {
                uRLBase = base;
            }
        } else {
            uRLBase = base;
        }
    }

    /** @see Object#toString() */
    public final String toString() {
        StringBuffer sb = new StringBuffer();
        
        sb.append("user:");
        sb.append(getUsername());
        sb.append("| pass:");
        sb.append(getPassword());
        sb.append("| repo:");
        sb.append(getRepository());
        sb.append("| saveData:");
        sb.append(isSaveData());
        sb.append("| startWithoutPrompting:");
        sb.append(isStartWithoutPrompting());
        sb.append("| savePass:");
        sb.append(isSavePassword());
        sb.append("| baseURL:");
        sb.append(getURLBase());
        sb.append("| proxy:");
        sb.append(getProxyHost()  + ":" + getProxyPort());
        
        return sb.toString();
    }

    /** @return el hostname del proxy */
    public final String getProxyHost() {
        return proxyHost;
    }

    /**
     * Setea el host donde corre el proxy
     * 
     * @param proxyHost el host donde corre el proxy
     */
    public final void setProxyHost(final String proxyHost) {
        if(proxyHost == null) {
            throw new IllegalArgumentException();
        }

        this.proxyHost = proxyHost;
    }

    /**
     * @return el puerto del proxy
     */
    public final int getProxyPort() {
        return proxyPort;
    }

    /**
     * Setea el puerto del proxy
     * 
     * @param proxyPort puerto del proxy
     */
    public final void setProxyPort(final int proxyPort) {
        if(proxyPort < 1) {
            throw new IllegalArgumentException(
                    "el puerto del proxy tiene que ser positivo");
        }
        this.proxyPort = proxyPort;
    }

    /** @return <code>true</code> si se debe filtrar los cursos */
    public final boolean isFilterCourses() {
        return filterCourses;
    }

    /**
     * @param filterCourses <code>true</code> si se deben filtrar los cursos
     */
    public final void setFilterCourses(final boolean filterCourses) {
        this.filterCourses = filterCourses;
    }

}
