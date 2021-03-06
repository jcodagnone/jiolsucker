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
package ar.com.leak.iolsucker;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import ar.com.zauber.commons.web.version.impl.ManifestVersionProvider;
import ar.com.zauber.commons.web.version.impl.PropertiesVersionProvider;

/**
 * Contiene informaci�n sobre el proyecto/programa
 * 
 * @author Juan F. Codagnone
 * @since Apr 12, 2005
 */
public class ProjectInfo {
    /** name */
    private final String projectName = "iolsucker";
    /** version... */
    private String projectVersion = "unknown";
    
    {
        InputStream is = null;
        try {
            is = getClass().getClassLoader().getResourceAsStream(
                    "META-INF/maven/ar.com.leak.iolsucker/jiol-iolsucker/pom.properties");
            if(is != null) {
                projectVersion = new PropertiesVersionProvider(is, "version").getVersion();
            }
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
    
    /** developers...*/
    private final String [] developers = {
            "Juan F. Codagnone <juan @ sin.spam im.leak.com.ar> (core)",
            "Eduardo Pereda (fixes GUI)",
    };
    /** description... */
    private final String projectDescription = projectName + " es la tercer "
      + "reencarnaci�n del gran iolsucker, un programa que automatiza la tarea "
      + "de mantenerse al d�a con los materiales did�cticos de ITBA On-Line\n"
      + "\nP�gina Web: http://www.leak.com.ar/~juan/code/jiol/";
    
    /** contributors... */
    private String [] contributors = {
            "Sebasti�n Thierer",
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
          + "\nPra m�s detalles consulte "
          + "http://www.apache.org/licenses/LICENSE-2.0";
        
    }
}
