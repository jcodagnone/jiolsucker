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
package ar.com.leak.iolsucker.view.common;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import junit.framework.TestCase;

import org.apache.commons.lang.Validate;

import ar.com.leak.common.fs.FilesystemUtils;
import ar.com.leak.iolsucker.impl.common.RelativeLocationValidator;
import ar.com.leak.iolsucker.impl.mock.MockCourse;
import ar.com.leak.iolsucker.impl.mock.MockIolDao;
import ar.com.leak.iolsucker.impl.mock.MockMaterialDir;
import ar.com.leak.iolsucker.impl.mock.MockMaterialFile;
import ar.com.leak.iolsucker.model.Course;
import ar.com.leak.iolsucker.model.IolDAO;
import ar.com.leak.iolsucker.model.Material;
import ar.com.leak.iolsucker.view.Repository;
import ar.com.leak.iolsucker.view.Repository.ObservableAction;

/**
 * testeo de unidad para FSRepository
 * 
 * @author Juan F. Codagnone
 * @since Apr 30, 2005
 */
public final class FSRepositoryTest extends TestCase {

    /**
     * @throws Exception on error
     */
    public void testEvilPaths() throws Exception {
       final IolDAO dao = new MockIolDao(new Course[] {
                new MockCourse("testeo", "10.1", -3, 
                        Arrays.asList(new Material [] {
                /* al tener null de data, si el repositorio lo quiere leer va a 
                 * saltar una excepcion 
                 */
                   new MockMaterialFile("../pepe", null), 
                   new MockMaterialDir(".."),
                   new MockMaterialDir("soy/malo/../../../../../proc/version"),
                   new MockMaterialFile("soy/malo/../../../../test", null),
                   new MockMaterialFile("soy/malo/../../../../", null),
                   new MockMaterialFile("directorio_valido/archivo", "buu"),
                   new MockMaterialDir("otro_directorio_valido"),
                }))
            }, null, null);
       // TODO no harcodear path
       final File location = new File(getTmpDirectory(), "evilPaths");
       if(!location.exists()) {
            location.mkdirs();
       }
        
        final Repository repository = new FSRepository(location,
                new NullDownloadMeter(), new RelativeLocationValidator(), 
                new NullRepublishRepositoryStrategy(), 1);

        /** TODO assert for the correct # */
        repository.addRepositoryListener(new Observer() {
            public void update(final Observable o, final Object arg) {
                Repository.ObservableAction action = (ObservableAction)arg;
                System.out.println(action.getType() + " -- " + action.getMsg());
                
            }
        });
        
        final Collection evilCourses = dao.getUserCourses();
        for(Iterator i = evilCourses.iterator(); i.hasNext();) {
            Course evilCourse = (Course)i.next();
            repository.syncMaterial(evilCourse);
        }
    }
    

    public void testSlashMateriaOkPaths() throws Exception {
        final IolDAO dao = new MockIolDao(new Course[] {
                 new MockCourse("Estadistica K/V", "10.1", -3, 
                         Arrays.asList(new Material [] {
                    new MockMaterialDir("otro_directorio_valido"),
                 }))
             }, null, null);
        // TODO no harcodear path
        final File location = new File(getTmpDirectory(), "evilPaths");
        if(!location.exists()) {
             location.mkdirs();
        }
         
         final FSRepository repository = new FSRepository(location,
                 new NullDownloadMeter(), new RelativeLocationValidator(), 
                 new NullRepublishRepositoryStrategy(), 1);
         
         for(Iterator i = dao.getUserCourses().iterator(); i.hasNext();) {
             Course evilCourse = (Course)i.next();
             repository.syncMaterial(evilCourse);
         }
         assertEquals(0, repository.getExceptions().size());
     }

    
    /**
     * testea que nos comportemos correctamente en una republicación de un 
     * archivo
     * 
     * @throws Exception on error
     */
    public void testRepublish() throws Exception {
        final File location = new File(getTmpDirectory(), "jiol-testRepublish");
        if(location.exists()) {
            FilesystemUtils.removeDir(location);
        }
        
        final FSRepository repository = new FSRepository(location,
                new NullDownloadMeter(), new RelativeLocationValidator(), 
                new TagRepublishRepositoryStrategy(), 1);
        
        // armo un curso que tiene un archivo. dos horas mas tarde se sube 
        // otro archivo, con mismo nombre
        final String courseName = "TESTS-101";
        final String courseCode = "1.23";
        final int courseLevel = Course.LEVEL_GRADO;
        final String fileName = "file1.txt";
        final Calendar calendar = GregorianCalendar.getInstance();
        final Date beforeDate = calendar.getTime();
        calendar.add(Calendar.HOUR, 2);
        final Date afterDate = calendar.getTime();
        final Date epochDate = new Date(0);
        
        final String []content = {
                "contenido 0",
                "contenido 1",
                "contenido 2"
        };
        
        final Course beforeCourse = new MockCourse(courseName, courseCode,
                courseLevel , Arrays.asList(new Material[] {
                    new MockMaterialFile(fileName, content[0], beforeDate)
                }));
        final Course afterCourse = new MockCourse(courseName, courseCode,
                courseLevel, Arrays.asList(new Material[] {
                    new MockMaterialFile(fileName, content[1], afterDate)
                }));
        final Course epochCourse = new MockCourse(courseName, courseCode,
                courseLevel, Arrays.asList(new Material[] {
                    new MockMaterialFile(fileName, content[2], epochDate)
                }));
        
        try {
            repository.syncMaterial(beforeCourse);
            repository.syncMaterial(afterCourse);
            repository.syncMaterial(epochCourse);
            
            assertEquals(repository.getDestDir(beforeCourse).listFiles().length,
                         2);
            
            assertEquals(content[1], // tuvo que haber quedado el contendo 1 
                         getFileContent(new File(
                               repository.getDestDir(beforeCourse), fileName)));
        } finally {
            if(location.exists()) {
                FilesystemUtils.removeDir(location);
            }
        }
    }


    /** @return el directorio temporario */
    private File getTmpDirectory() {
        final String tmpDir = System.getProperty("java.io.tmpdir");
        Validate.notNull(tmpDir, "java.io.tmpdir is not set!!");
        return new File(tmpDir);
    }
    
    /**
     * @param f el archivo a abrir
     * @return el contenido de f
     * @throws IOException on error
     */
    private String getFileContent(final File f) throws IOException {
        final StringBuilder sb = new StringBuilder();
        
        final Reader reader = new FileReader(f);
        final int bufferSize = 1024;
        final char []buff = new char[bufferSize];
        int len;
        
        while((len = reader.read(buff)) != -1) {
            sb.append(buff, 0, len);
        }
        reader.close();
        
        return sb.toString();
    }
}
