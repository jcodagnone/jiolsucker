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
import java.util.Date;

import ar.com.leak.iolsucker.model.Material;


/**
 * Implementacion mock de Material
 * 
 * @author Juan F. Codagnone
 * @since Mar 1, 2005
 * @see Material
 */
public class MockMaterialDir implements Material {
    /** nombre del directorio */
    private final String name;

    /**
     * Crea el MockMaterialDir.
     *
     * @param name nombre del directorio
     */
    public MockMaterialDir(final String name) {
        this.name = name;
    }
    
    /** @see Material#isFolder() */
    public final boolean isFolder() {
        return true;
    }

    /** @see Material#getName() */
    public final String getName() {
        return name;
    }

    /** @see Material#getInputStream() */
    public final InputStream getInputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    /** @see Material#getEstimatedSize() */
    public final long getEstimatedSize() {
        throw new UnsupportedOperationException();
    }
    
    /** @see java.lang.Object#toString() */
    public final String toString() {
        return name;
    }

    /** @see ar.com.leak.iolsucker.model.Material#getLastModified() */
    public final Date getLastModified() {
        return new Date(0);
    }
}