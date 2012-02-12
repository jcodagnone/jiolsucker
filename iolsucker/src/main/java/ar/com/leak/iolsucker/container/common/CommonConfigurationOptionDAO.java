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
package ar.com.leak.iolsucker.container.common;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.leak.iolsucker.container.Options;
import ar.com.leak.iolsucker.container.OptionsDAO;

/**
 * 
 * <code>OptionsDAO</code> que almacena la opciones en un archivo xml usando
 * el paquete Commons-Configuration 
 *
 * Por defecto el archivo se almacena relativo al directorio apuntado por 
 * <code>user.home</code> pero puede cambiarlo con <code>iolsucker.home</code>.
 *  
 * @see ar.com.leak.iolsucker.container.OptionsDAO
 * @author Juan F. Codagnone
 * @since Apr 12, 2005
 */
public class CommonConfigurationOptionDAO extends FileOptionsDAO {
    /** class logger */
    private final Logger logger = LoggerFactory.getLogger(
            CommonConfigurationOptionDAO.class);
    /** nombre del directorio donde se guarda la configuración */
    public static final String IOL_DIRECTORY = ".jiolsucker";
    /** nombre del archivo dentro de IOL_DIRECTORY donde se guarda la 
     *  configuración  */
    private static final String IOL_CONFIG_FILE = "config.xml";
    

    /** Crea el CommonConfigurationOptionDAO. */
    public CommonConfigurationOptionDAO() {
        super(getConfigFile(IOL_DIRECTORY, IOL_CONFIG_FILE));
    }

    /**
     * @param defaultDir directoroio default donde buscar
     * @param defaultFile nombre del archivo por default de la configuracio
     * @return el archivo que por defecto contiene la configuración de IOL
     */
    public static File getConfigFile(final String defaultDir, 
            final String defaultFile) {
        String homeDir = System.getProperty("user.home");
        String customDir = System.getProperty("iolsucker.home");
        
        if(customDir != null && customDir.trim().length() != 0) {
            homeDir = customDir;
        } else { 
            if(homeDir == null) {
                throw new IllegalArgumentException("no se pudo determinar "
                        + "cual es  el directorio home del usuario");
            }
        }
        File dir = new File(homeDir, defaultDir);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, defaultFile);
    }

    
    /**
     * Crea el CommonConfigurationOptionDAO.
     *
     * @param file archivo donde se guardan las opciones 
     */
    public CommonConfigurationOptionDAO(final File file) {
        super(file);
    }
   
    /** 
     * número de versión del tipo del archivo donde se guarda la configuración.
     *  Se debe incrementar cada vez que se hace un cambio incompatible 
     */
   private static final int REVISION = 3; 
   /** llave de la revisión */
   private static final String KEY_REVISION = "revision";
   /** llave dice cual es el nombre de usuario */
   private static final String KEY_USER = "username";
   /** llave dice cual es la password del usuario */
   private static final String KEY_PASS = "password";
   /** llave que dice si se guarda la conf o no */
   private static final String KEY_SAVE_DATA = "save_data";
   /** llave que dice si se inicia el download sin preguntar */
   private static final String KEY_START_WITHOUT_PROMPTING = 
       "start_without_prompting";
   /** llave que dice si se guarda la password o no */
   private static final String KEY_SAVE_PASS = "save_password";
   /** llave del path del repositorio*/
   private static final String KEY_REPO = "repo";
   /** llave que dice el algoritmo en el que está cifrada la password */
   private static final String KEY_PASS_ALGO = "pass_algo";
   /** llave que dice la url base de IOL */
   private static final String KEY_URLBASE = "url_base";
   /** llave que dice el host del proxy */
   private static final String KEY_PROXYHOST = "proxy_host";
   /** llave que dice el puerto del proxy */
   private static final String KEY_PROXYPORT = "proxy_port";
   /** llave que dice si se deben filtrar los cursos */
   private static final String KEY_FILTERCOURSES  = "filter_courses";
   /** llave que dice si transferimos como el viejo iolsucker-2.x */
   
   /** @see OptionsDAO#getOptions()*/
    public final Options getOptions() throws MalformedURLException {
        final Options ret = new Options();
        final XMLConfiguration config;
        try {
            config = new XMLConfiguration(getFile());
            int rev =  config.getInt(KEY_REVISION);
            if(rev > REVISION) {
                logger.warn("la configuración es una versión mas nueva de "
                        + "la que sabemos manejar, ignorando");
            } else {
                ret.setUsername(config.getString(KEY_USER));
                ret.setSaveData(config.getBoolean(KEY_SAVE_DATA));
                ret.setSavePassword(config.getBoolean(KEY_SAVE_PASS));
                ret.setRepository(config.getString(KEY_REPO));

                final String pass = config.getString(KEY_PASS);
                final String algo = config.getString(KEY_PASS_ALGO);
                if(pass != null) {
                    ret.setPassword(PasswordCoder.decode(algo, pass));
                }

                if(rev > 0) {
                    String url = config.getString(KEY_URLBASE).replaceAll("^http:[/][/]iol2[.]itba[.]edu[.]ar:27521[/]$", "http://iol2.itba.edu.ar/");
                    ret.setURLBase(new URL(url));
                    
                    String s = config.getString(KEY_PROXYHOST);
                    ret.setProxyHost(s == null ? "" : s);
                    ret.setProxyPort(config.getInt(KEY_PROXYPORT));
                }
                if(rev > 1) {
                    ret.setFilterCourses(config.getBoolean(KEY_FILTERCOURSES));
                }
                if(rev > 2) {
                    ret.setStartWithoutPrompting(config
                            .getBoolean(KEY_START_WITHOUT_PROMPTING));
                }
            }

        } catch(ConfigurationException e) {
            logger.debug("cargando configuracion", e);
        }
    
        return ret;
    }

    /** @see OptionsDAO#saveOptions(Options) */
    public final void saveOptions(final Options options) throws Exception {
        final XMLConfiguration config = new XMLConfiguration();
        config.addProperty(KEY_REVISION, new Integer(REVISION));
        config.addProperty(KEY_USER, options.getUsername());
        if(options.isSavePassword()) {
            config.addProperty(KEY_PASS, PasswordCoder.code(PasswordCoder
                    .getPreferedAlgorithm(), options.getPassword()));    
        } else {
            config.addProperty(KEY_PASS, "");
        }
        
        config.addProperty(KEY_PASS_ALGO, PasswordCoder.getPreferedAlgorithm());
        config.addProperty(KEY_SAVE_DATA, new Boolean(options.isSaveData()));
        config.addProperty(KEY_START_WITHOUT_PROMPTING, 
                new Boolean(options.isStartWithoutPrompting()));
        config.addProperty(KEY_SAVE_PASS,
                new Boolean(options.isSavePassword()));
        config.addProperty(KEY_REPO, options.getRepository());
        config.addProperty(KEY_URLBASE, options.getURLBase());

        config.addProperty(KEY_PROXYHOST, options.getProxyHost());
        config.addProperty(KEY_PROXYPORT, Integer.toString(
                options.getProxyPort()));
        config.addProperty(KEY_FILTERCOURSES,
                new Boolean(options.isFilterCourses()));
        config.save(getFile());
    }

}
