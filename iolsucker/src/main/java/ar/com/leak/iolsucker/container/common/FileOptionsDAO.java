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

import ar.com.leak.iolsucker.container.OptionsDAO;

/**
 * Abstracción para todas los OptionsDAO que almacenan sus datos en un archivo
 * 
 * @author Juan F. Codagnone
 * @since Apr 12, 2005
 */
public abstract class FileOptionsDAO implements OptionsDAO {
    /** archivo donde se guardan los datos */
    private final File file;
    
    /**
     * Crea el FileOptionsDAO.
     *
     * @param file donde se guardan los datos
     */
    public FileOptionsDAO(final File file) {
        this.file = file;
    }
    
    /** @see OptionsDAO#clearOptions() */
    public final void clearOptions() throws Exception {
        file.delete();
    }

    /** @return el archivo donde se guardan las cosas */
    public final File getFile() {
        return file;
    }
}
