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
package ar.com.leak.iolsucker.view.common;

import java.io.File;
import java.util.*;

import org.apache.commons.lang.Validate;

import ar.com.leak.iolsucker.model.Material;


/**
 * Implementación de <code>RepublishRepositoryStrategy</code> que renombra
 * el archivo existente a uno en el mismo directorio, pero agregandole antes
 * de la extensión 
 * <p/>
 * No me gusta mucho, porque estamos esperando que nadie publique un archivo
 * con el mismo nombre...por hay lo mejor es hacer otra estrategia que mueva
 * los archivos a un atico.
 * 
 * @author Juan F. Codagnone
 * @since Sep 6, 2005
 */
public class TagRepublishRepositoryStrategy 
          implements RepublishRepositoryStrategy {
    /** mensaje para agregarle al archivo viejo */
    private final String tag;

    /** @see #OldRepublishRepositoryStrategy(String) */
    public TagRepublishRepositoryStrategy() {
        this("old_republicado_");
    }
    
    /**
     * Creates the OldRepublishRepositoryStrategy.
     *
     * @param tag mensaje a agregarle al archivo viejo
     */
    public TagRepublishRepositoryStrategy(final String tag) {
        Validate.notNull(tag, "tag");
        Validate.notEmpty(tag);
        
        this.tag = tag;
    }
    
    /** @see RepublishRepositoryStrategy#republish(Material, java.io.File) */
    public final void republish(final Material material, 
            final File existingFile) {

        if(!existingFile.renameTo(getNewFile(existingFile))) {
            throw new RuntimeException("ehh?");
        }
    }
    
    /**
     * @param existingFile un archivo que ya existe
     * @return un nuevo nombre para renombrar el archivo que ya existia.
     */
    private  File getNewFile(final File existingFile) {
        final String name = existingFile.getName();
        int index = name.lastIndexOf('.');
        if(index == -1) {
            index = 0;
        }
        final Formatter formatter = new Formatter();
        final Calendar lastModifed = GregorianCalendar.getInstance();
        lastModifed.setTimeInMillis(existingFile.lastModified());
        
        File ret = null;
        for(int id = 0; ret == null; id++) {
            final String newFileName = formatter.format(
                    "%s.%s%04d%02d%02d_%d.%s", 
                    name.substring(0, index),
                    tag,
                    lastModifed.get(Calendar.YEAR),
                    lastModifed.get(Calendar.MONTH),
                    lastModifed.get(Calendar.DAY_OF_MONTH),
                    id,
                    name.substring(index + 1)).toString();
            ret = new File(existingFile.getParentFile(), newFileName);
            if(ret.exists()) {
                ret = null;
            }
        }
        return ret; 
    }
}
