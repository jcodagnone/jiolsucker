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
package ar.com.leak.iolsucker.impl.mock;

import java.util.Collection;
import java.util.Collections;

import ar.com.leak.iolsucker.model.Course;
import ar.com.leak.iolsucker.model.Material;

/**
 * implementación tonta para testeos de course
 * 
 * @author Juan F. Codagnone
 * @since Apr 30, 2005
 */
public class MockCourse implements Course {
    /** archivos inyectados */
    private final Collection<Material> files;
    /** nivel inyectado */
    private final int level;
    /** code inyectado */
    private final String code;
    /** name inyectado */
    private final String name;

    /**
     * Crea el MockCourse.
     *
     * @param name nombre de la materia. Ej: ELECTROTECNIA
     * @param code codigo de la materia. Ej: 71.04
     * @param level nivel de la materia. Ej: 4 para materias de grado
     * @param files collection de Material.
     */
    public MockCourse(final String name, final String code, final int level,
            final Collection<Material> files) {
        this.name = name;
        this.code = code;
        this.level = level;
        this.files = files;
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
    public final Collection<Material> getFiles() {
        return Collections.unmodifiableCollection(files);
    }
}
