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
/*
 * Originally from rev 267218 of http://svn.apache.org/repos/asf/maven/
 *  components/trunk/maven-plugins/maven-clean-plugin/src/main/java/
 *  org/apache/maven/plugin/clean/CleanPlugin.java
 *  
 *  original author 
 *     <a href="mailto:evenisse@maven.org">Emmanuel Venisse</a>
 */
package ar.com.leak.common.fs;

import java.io.File;


/**
 * @author Juan F. Codagnone (tailor the class to be reused)
 * @author <a href="mailto:evenisse@maven.org">Emmanuel Venisse</a>
 * @since Sep 6, 2005
 */
public final class FilesystemUtils {
    /** time to wait */
    private static final int DELETE_RETRY_SLEEP_MILLIS = 10;
    
    /** Creates the FilesystemUtils. */
    private FilesystemUtils() {
        // utility class
    }
    
    /**
     * Removes (recursively a directory
     * 
     * @param dir directory to remove
     */
    public static void removeDir(final File dir) {
        String [] list = dir.list();
        if(list == null) {
            list = new String[0];
        }
        
        for(int i = 0; i < list.length; i++) {
            String s = list[i];
            File f = new File(dir, s);
            if(f.isDirectory()) {
                removeDir(f);
            } else {
                if(!delete(f)) {
                    throw new RuntimeException("Unable to delete file "
                            + f.getAbsolutePath());
                }
            }
        }

        if(!delete(dir)) {
            throw new RuntimeException("Unable to delete directory "
                    + dir.getAbsolutePath());
        }
    }

    
    /**
     * Accommodate Windows bug encountered in both Sun and IBM JDKs.
     * Others possible. If the delete does not work, call System.gc(),
     * wait a little and try again.
     * 
     * @param file to remove
     * @return <code>true</code> on succsess
     */
    private static boolean delete(final File file) {
        if(!file.delete()) {
            if(System.getProperty("os.name").toLowerCase().
                    indexOf("windows") > -1) {
                System.gc();
            }
            try {
                Thread.sleep(DELETE_RETRY_SLEEP_MILLIS);
                return file.delete();
            } catch(InterruptedException ex) {
                return file.delete();
            }
        }
        return true;
    }
    
    /**
     * crea directorio chequeando si existia antes. no es realmente necesario
     * pero hace saltar errores en el filesystem como directorios montados solo
     * lectura tira Illegal
     * 
     * @param dir directory to create
     */
    public static void mkdir(final File dir) {
        if(dir.exists()) {
            if(!dir.isDirectory()) {
                throw new IllegalStateException(dir.getAbsolutePath()
                        + " deberia ser un archivo pero es un directorio");
            }
        } else {
            if(!dir.mkdirs()) {
                throw new IllegalStateException(dir.getAbsolutePath()
                        + " no se pudo crear");
            }
        }
    }
}
