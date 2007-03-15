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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.com.leak.iolsucker.model.Course;
import ar.com.leak.iolsucker.model.Material;

/**
 * Implementación http de un Curso
 * 
 * @author Juan F. Codagnone
 * @since Feb 25, 2005
 * @see ar.com.leak.iolsucker.model.Course
 * @has - - - ar.com.leak.iolsucker.impl.http.HTTPMaterialFactory
 * @depend - param - ar.com.leak.iolsucker.impl.http.HTTPClient
 */
public class HTTPCourse implements Course {
    /** class logger */
    private final Log log = LogFactory.getLog(HTTPCourse.class);
    /** injected course context link */
    private String link;

    /** injected course code */
    private final String code;
    /** injected course name */
    private final String name;
    /** injected course level */
    private final int level;
    /** injected HTTPClient */
    private final HTTPClient httpClient;
    /** injected HTTPMaterialFactory */
    private final HTTPMaterialFactory materialFactory;

    /**
     * Crea el HTTPCourse.
     *
     * @param httpClient httpClient en uso
     * @param materialFactory materialFactory que se usará
     * @param code  codigo de la materia. Ej 71.04
     * @param name  nombre de la materia. Ej. "Sistemas de computación"
     * @param level nivel de la materia. Ej. -4 para carreras de grado 
     */
    public HTTPCourse(final HTTPClient httpClient,
            final HTTPMaterialFactory materialFactory, final String code, 
            final String name, final int level) {
        this.httpClient = httpClient;
        this.materialFactory = materialFactory;
        this.code = code;
        this.name = name;
        this.level = level;

        if(httpClient == null || code == null || name == null
                || code.trim().length() == 0 || name.trim().length() == 0) {
            log.debug(code);
            log.debug(name);
            throw new IllegalArgumentException("construyendo el curso");
        }
    }

    /** @see ar.com.leak.iolsucker.model.Course#getName() */
    public final String getName() {
        return name;
    }

    /** @see ar.com.leak.iolsucker.model.Course#getCode() */
    public final String getCode() {
        return code;
    }

    /** @see ar.com.leak.iolsucker.model.Course#getLevel() */
    public final int getLevel() {
        return level;
    }

    /** @see ar.com.leak.iolsucker.model.Course#getFiles() */
    public final Collection<Material>  getFiles() {
        Collection<Material>  ret = new ArrayList<Material>();

        try {
            changeContext();

            ret = materialFactory.getMaterial(httpClient);

        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        return ret;
    }

    /** @see Object#toString() */
    public final String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append('[');
        sb.append(getCode());
        sb.append('|');
        sb.append(getLevel());
        sb.append('|');
        sb.append(getName());
        sb.append(']');

        return sb.toString();
    }

    /**
     * Setea el link para cambiar de contexto de la materia
     * 
     * @param link link que cambia de contexto de la materia
     */
    public final void setLink(final String link) {
        this.link = link;
    }

    /**
     * Cambia el contexto de IOL al de esta materia.
     * @throws IOException on error
     */
    private void changeContext() throws IOException {
        if(link == null) {
            throw new IllegalStateException("no se cual es el link para "
                    + "cambiar de contexto! error de programación!");
        }
        URLConnection huc = httpClient.getConnection(link);
        huc.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(huc
                .getInputStream()));

        while(in.readLine() != null) {
            // void
        }
        in.close();
    }

    /** @see Object#equals(java.lang.Object) */
    public final boolean equals(final Object obj) {
        boolean ret;

        if(obj == this) {
            ret = true;
        } else if(obj instanceof HTTPCourse) {
            HTTPCourse c = (HTTPCourse)obj;

            ret = c.getLevel() == getLevel() && c.getName().equals(getName())
                    && c.getCode().equals(getCode());
        } else {
            ret = false;
        }

        return ret;
    }

    /** @see Object#hashCode() */
    public final int hashCode() {
        final int magicInit = 37;
        final int magicMultiplier = 17;
        
        int ret = magicInit;
        ret += magicMultiplier * getLevel();
        ret += magicMultiplier * getName().hashCode();
        ret += magicMultiplier * getCode().hashCode();

        return ret;
    }
}
