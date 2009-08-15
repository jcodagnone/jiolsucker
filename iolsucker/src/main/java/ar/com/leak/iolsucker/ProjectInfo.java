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
package ar.com.leak.iolsucker;

/**
 * Contiene información sobre el proyecto/programa
 * 
 * TODO generar desed el POM de maven.
 * 
 * @author Juan F. Codagnone
 * @since Apr 12, 2005
 */
public class ProjectInfo {
    /** name */
    private final String projectName = "iolsucker";
    /** version... */
    private final String projectVersion = "3.17";
    /** developers...*/
    private final String [] developers = {
            "Juan F. Codagnone <juan @ sin.spam im.leak.com.ar> (core)",
            "Eduardo Pereda (fixes GUI)",
    };
    /** description... */
    private final String projectDescription = projectName + " es la tercer "
      + "reencarnación del gran iolsucker, un programa que automatiza la tarea "
      + "de mantenerse al día con los materiales didácticos de ITBA On-Line\n"
      + "\nPágina Web: http://www.leak.com.ar/~juan/code/jiol/";
    
    /** contributors... */
    private String [] contributors = {
            "Sebastián Thierer",
            "Federico Angeleri",
    };
    
    /** @return the project's name */
    public final String getProjectName() {
        return projectName;
    }
    
    /** @return the project's version */
    public final String getProjectVersion() {
        return projectVersion;
    }
    
    /** @return the project's  description */
    public final String getProjectDescription() {
        return projectDescription;
    }
    
    /** @return the project's developers */
    public final String[] getDevelopers() {
        return developers;
    }
    
    /** @return the project's contributors */
    public final String[] getContributors() {
        return contributors;
    }
    
    /** @return the project's license */
    public final String getLicence() {
        return "Se distribuye bajo la  Apache License Version 2.0\n"  
          + "\nPra más detalles consulte "
          + "http://www.apache.org/licenses/LICENSE-2.0";
        
    }
}
