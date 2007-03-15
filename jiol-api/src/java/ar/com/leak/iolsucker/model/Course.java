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
package ar.com.leak.iolsucker.model;

import java.util.Collection;


/**
 * Representa a una materia / curso de IOL
 * 
 * @author Juan F. Codagnone
 * @since Feb 25, 2005
 * @has - - n ar.com.leak.iolsucker.model.Material 
 */
public interface Course {

    /**
     * @return el nombre del recurso. El nombre es un path. Por ejemplo:
     *          "practicas/tp1/Ej1.pdf". No debe tener una / al principio.
     */
    String getName();
    
    /**
     * @return el código de la materia. Ej: "71.04"
     */
    String getCode();
    
    /**
     * @return el nivel de la materia. tipicamente 4 para materias de grado
     */
    int getLevel();
    
    /**
     * 
     * @return una colleción con elementos de tipo <code>Material</code>, con
     *         el material didactico de la materia. 
     * @see Material
     */
    Collection <Material> getFiles();
    
    /** entero que corresponde a materias de grado...todo: enum */
    int LEVEL_GRADO = 4;
}
