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
package ar.com.leak.iolsucker.view;

import java.io.File;
import java.util.List;
import java.util.Observer;

import ar.com.leak.iolsucker.model.Course;


/**
 * Representación del repositorio local. En el repositorio se almacenan
 * los archivos.
 * 
 * @author Juan F. Codagnone
 * @since Feb 26, 2005
 */
public interface Repository {
    /**
     * Mensaje que le avisa al repositorio que se tiene esta materia.
     *  
     * @param course un curso
     */
    void touch(Course course);
    
    /**
     * Sincroniza el material del curso en el repositorio (tipicamente
     * descarga los archivos)
     * 
     * @param course un curso
     */
    void syncMaterial(Course course);
    
    /**
     * @param observer observer. El argumento que deben recibir es del tipo 
     *                 ObservableAction 
     */
    void addRepositoryListener(Observer observer);
    
    /**
     * @param observers list of observers to add
     */
    void setRepositoryListeners(List /*Observer*/ observers);
    
    /**
     * Una acción observable del repositorio
     * 
     * @author Juan F. Codagnone
     * @since Aug 3, 2005
     */
    public class ObservableAction {
        /** un mensaje...*/
        private final String msg;
        /** un tipo de acción...*/
        private final ObservableActionEnum type;
        /** el archivo que se modificó */
        private final File file;

        /**
         * Argumento pasado a los observers
         *
         * @param msg un mensaje
         * @param type un tipo de acción
         * @param file archivo que cambió
         */
        public ObservableAction(final String msg, 
                final ObservableActionEnum type, 
                final File file) {
            this.msg = msg;
            this.type = type;
            this.file = file;
        }

        /** @return el mensaje */
        public final String getMsg() {
            return msg;
        }

        /** @return un tipo de acción */
        public final ObservableActionEnum getType() {
            return type;
        }
        
        /** @return the file */
        public final File getFile() {
            return file;
        }
    }
    
    /**
     * Type safe enum con las acciones observables por el navegador
     * 
     * @author Juan F. Codagnone
     * @since Aug 3, 2005
     */
    public static final class ObservableActionEnum {
        /** name of the enum entry*/
        private final String name;
       
        /** se creó un nuevo directorio */
        public static final ObservableActionEnum NEW_FOLDER = 
                             new ObservableActionEnum("nueva carpeta");
        /** se creó un nuevo archivo */
        public static final ObservableActionEnum NEW_FILE = 
                             new ObservableActionEnum("nuevo archivo");
        
        /** se republicó un nuevo archivo */
        public static final ObservableActionEnum REPUBLISHED_FILE = 
                             new ObservableActionEnum("se republicó archivo");
        
        /**
         * Crea el ObservableActionEnum.
         *
         * @param name name of the enum entry
         */
        private ObservableActionEnum(final String name) {
            this.name = name;
        }
        
        /** @see Object#toString() */
        public String toString() {
            return name;
        }
    }
}
