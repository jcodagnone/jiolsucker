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
package ar.com.leak.iolsucker.impl.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.lang.NullArgumentException;

import ar.com.leak.iolsucker.model.Material;


/**
 * Implementación HTTP de un Material Directorio
 * 
 * @author Juan F. Codagnone
 * @since Feb 26, 2005
 */
public class HTTPMaterialFolder implements Material {
    /** nombre de la carpeta */
    private final String name;

    /**
     * Crea el HTTPMaterialFolder.
     *
     * @param name path completo de la carpeta
     */
    public HTTPMaterialFolder(final String name) {
        if(name == null) {
            throw new NullArgumentException("name no puede ser nulo");
        }
        
        this.name = name;
    }
    
    /** @see ar.com.leak.iolsucker.model.Material#isFolder() */
    public final boolean isFolder() {
        return true;
    }
    
    /**@see ar.com.leak.iolsucker.model.Material#getInputStream() */
    public final InputStream getInputStream() throws IOException {
        throw new UnsupportedOperationException();
    }
    
    /** @see ar.com.leak.iolsucker.model.Material#getName() */
    public final String getName() {
        return name;
    }

    /** @see ar.com.leak.iolsucker.model.Material#getEstimatedSize() */
    public final long getEstimatedSize() {
        throw new UnsupportedOperationException();
    }

    /** @see java.lang.Object#toString() */
    public final String toString() {
        return getName();
    }

    /** @see ar.com.leak.iolsucker.model.Material#getLastModified() */
    public final Date getLastModified() {
        // TODO ver si se puede implementar
        return new Date(0);
    }
}
