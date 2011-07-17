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
package ar.com.leak.iolsucker.impl.mock;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Date;

import ar.com.leak.iolsucker.model.Material;

/**
 * Implementacion mock de Material
 * 
 * @author Juan F. Codagnone
 * @since Mar 1, 2005
 * @see Material
 */
public class MockMaterialFile implements Material {
    /** path completo del archivo */
    private final String name;
    /** contenido del archivo */
    private final String data;
    /** ultima fecha de modificación */
    private final Date lastModDate;

    /**
     * Crea el MockMaterialFile.
     *
     * @param name path completo del archivo. Ej: practica1/ej1.txt
     * @param data contenido del archivo
     * @param lastModDate ultima fecha de modificacion 
     */
    public MockMaterialFile(final String name, final String data, 
            final Date lastModDate) {
        this.name = name;
        this.data = data;
        this.lastModDate = lastModDate;
    }
    
    /** @see #MockMaterialFile(String, String, Date) */
    public MockMaterialFile(final String name, final String data) { 
        this(name, data, new Date(0));
    }
    
    /** @see Material#isFolder() */
    public final boolean isFolder() {
        return false;
    }

    /** @see Material#getName() */
    public final String getName() {
        return name;
    }

    /** @see Material#getInputStream() */
    public final InputStream getInputStream() throws IOException {
        if(data == null) {
            throw new IOException("null data");
        }
        
        return new StringBufferInputStream(data);
    }

    /** @see Material#getEstimatedSize() */
    public final long getEstimatedSize() {
        return data.length();
    }
    
    /** @see java.lang.Object#toString() */
    public final String toString() {
        return name;
    }

    /** @see ar.com.leak.iolsucker.model.Material#getLastModified() */
    public final Date getLastModified() {
        return lastModDate;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
