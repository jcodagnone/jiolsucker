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
package ar.com.leak.iolsucker.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;


/**
 * Represena al material didáctico de una materia. Puede ser tanto un
 * directorio como un archivo
 * 
 * @author Juan F. Codagnone
 * @since Feb 25, 2005
 */
public interface Material {
    
    /**
     * @return <code>true</code> si el material es un directorio
     */
    boolean isFolder();
    
    /**
     * @return nombre del material. es un path relativo.
     *  Ej: "practicas/tp1/Ej1.pdf"
     */
    String getName();
    
    /**
     * @return un <code>InputStream</code> que permitirá leer el contenido del
     *        material
     * 
     * @throws IOException si hubo problemas en conseguir el stream
     */
    InputStream getInputStream() throws IOException;
    
    /**
     * 
     * @return el tamaño estimado del archivo. Debe retornar 0 o un número 
     * negativo si se desconoce
     */
    long getEstimatedSize();
    
    /**
     * @return la fecha de la ultima modificación del archivo.
     *         Si no está disponible, la convención es retornar la fecha del 
     *         inicio de la epoch (unix!)
     */
    Date getLastModified();
}
