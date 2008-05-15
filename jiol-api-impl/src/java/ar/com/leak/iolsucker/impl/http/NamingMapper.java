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
package ar.com.leak.iolsucker.impl.http;

import java.net.MalformedURLException;
import java.net.URL;

import ar.com.leak.iolsucker.impl.http.util.URIHelper;

/**
 * Conoce las url de los comandos web de IOL.
 * 
 * @author Juan F. Codagnone
 * @since Feb 25, 2005
 */
public class NamingMapper {
    /** base url de todas las operaciones */ 
    private final URL urlBase;

    /**
     * Crea el NamingMapper.
     * 
     * @param urlBase
     *            http://silvestre.itba.edu.ar/itbaV/
     * @throws MalformedURLException
     *             si base no parece una URL..
     */
    public NamingMapper(final URL urlBase) throws MalformedURLException {
        this.urlBase = urlBase;
    }

    /** @return la url base de IOL */
    public final URL getURLBase() {
        return urlBase;
    }

    /** @return el comando para logearse */
    public final String getLoginCommand() {
        return "mynav.asp";
    }

    /** @return el comando para desloguearse */
    public final String getLogoutCommand() {
        return "mynav.asp?cmd=logout";
    }

    /** @return el comando para obtener la lista de cursos */
    public final String getUserCoursesCommand() {
        return getLoginCommand();
    }

    /** @return el comando para cambiar de curso actual */
    public final URIHelper getChangeContextCommandBase() {
        return new URIHelper("mynav.asp?cmd=ChangeContext");
    }

    /** @return el comando para obtener el material didactico */
    public final String getCourseFilesCommand() {
        return "newmaterialdid.asp";
    }

    /** 
     * @param fid file id a bajar
     * @return el comando para bajar un archivo
     */
    public final String getDownloadFile(final int fid) {
        return "startDownload.asp?fiid=" + fid;
    }

    /** 
     * @param fid file id del que se quiere el header
     * @return el comando para bajar un archivo
     */
    public final String getFileHeader(final int fid) {
        return "showfile_header.asp?fiid=" + fid;
    }
    
    /**
     * @param all <code>true</code> si se quiere obtener todas las noticias
     *        (leidas + no leidas)
     * @return el comando para obtener noticias
     */
    public final String getNewsCommand(final boolean all) {
        return "novlistAll.asp?showAll=" + Boolean.toString(all);
    }
    
    /** pagina de login */
    public final String getWelcomePage() {
        return "welcome.asp";
    }
}
