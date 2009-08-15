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

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.collections.*;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.com.leak.common.fs.FilesystemUtils;
import ar.com.leak.iolsucker.impl.common.Validator;
import ar.com.leak.iolsucker.model.Course;
import ar.com.leak.iolsucker.model.Material;
import ar.com.leak.iolsucker.view.DownloadMeter;
import ar.com.leak.iolsucker.view.Repository;

/**
 * Implementación del repositorio que almacena los archivos en el filesystem
 * de la misma manera que iolsucker-2.x
 * 
 * @author Juan F. Codagnone
 * @since Feb 26, 2005
 */
public class FSRepository extends  Observable implements Repository {
    /** logger instance */
    private final Log logger = LogFactory.getLog(FSRepository.class);
    /** Path al repositorio (injected) */
    private final File base;
    /** list of Course that where touched */
    private final Set<Course> setTouch = new HashSet<Course>();
    /** download meter to use */
    private final DownloadMeter meter;
    /** relative path validator to use (injected) */
    private final Validator relativePathValidator;
    /** cantidad de trabajadores para obtener archivos y directorios en 
     * paralelo */ 
    private final int nWorkers;
    /** estrategia de republicación de archivos */
    private final RepublishRepositoryStrategy republishStrategy;
    
    /**
     * Crea el FSRepository.
     *
     * @param base Path al repositorio
     * @param downloadMeter download meter to use
     * @param relativePathValidator relative path validator to use
     * @param republishStrategy estrategia de republicación de archivos
     * @param nWorkers cantidad de threads pararalelos a tener a tener cuando
     *                 pruebo si un archivo es nuevo.
     */
    public FSRepository(final File base, final DownloadMeter downloadMeter,
            final Validator relativePathValidator, 
            final RepublishRepositoryStrategy republishStrategy,
            final int nWorkers) {
        
        if(base == null || downloadMeter == null || republishStrategy == null) {
            throw new NullArgumentException("no se permiten parametro nulos");
        } else if(nWorkers < 1) {
            throw new IllegalArgumentException("la cantidad de trabajadores " 
                    + "debe ser por lo menos 1");
        }
        
        this.base = base;
        this.meter = downloadMeter;
        this.relativePathValidator = relativePathValidator;
        this.nWorkers = nWorkers;
        this.republishStrategy = republishStrategy;
        
        if(base.exists()) {
            base.delete();
        }
        base.mkdirs();
    }

    /** @see Repository#touch(ar.com.leak.iolsucker.model.Course) */
    public final void touch(final Course course) {
        setTouch.add(course);
    }

    /** @see Repository#syncMaterial(ar.com.leak.iolsucker.model.Course) */
    public final void syncMaterial(final Course course) {
        touch(course);
        
        final Queue<Material> courseFiles = 
            new ConcurrentLinkedQueue<Material>();
        courseFiles.addAll(course.getFiles());

        final Collection <Material>newFiles = new Vector<Material>();
        final Collection <Material>republishedFiles = new Vector<Material>();
        
        if(courseFiles.size() > 0) {
            // segun la implementación getName() puede tardar un poco (se debe
            // conectar a la red... ) por eso paralelizo el trabajo. Es medio 
            // feo tener que hacer esto (se pierde un poco de la abstracción)
            int realWorkers = courseFiles.size() > nWorkers  
                    ? nWorkers : courseFiles.size();
            final CountDownLatch doneSignal = new CountDownLatch(realWorkers);
            for(int i = 0; i < realWorkers; i++) {
                new Thread(new Runnable() {
                    public void run() {
                        Material material;
                        while((material = courseFiles.poll()) != null) {
                            final File d = getDestDir(course, material);
                            
                            if(!material.isFolder() && d != null) {
                                if(d.exists()) {
                                    if(material.getLastModified().after(
                                            new Date(d.lastModified()))) {
                                        republishedFiles.add(material);
                                    }
                                } else {
                                    newFiles.add(material);
                                }
                            }
                        }
                        doneSignal.countDown();
                    }
                }).start();
            }
            try {
                doneSignal.await();
            } catch(final InterruptedException e1) {
                throw new RuntimeException(e1);
            }
        }
        
        final Collection newDirs = CollectionUtils.select(courseFiles,
                new Predicate() {
                    public boolean evaluate(final Object data) {
                        final Material material = (Material)data;
                        File d = getDestDir(course, material);
                        return material.isFolder() && d != null && !d.exists();
                    }
                });
        
        CollectionUtils.forAllDo(newDirs, new Closure() {
            public void execute(final Object data) {
                final Material material = (Material)data;
                final File destDir = getDestDir(course, material);
                if(destDir != null) {
                    logger.debug("creando dir " + destDir);
                    FilesystemUtils.mkdir(destDir);
                    setChanged();
                    notifyObservers(
                            new Repository.ObservableAction("nueva carpeta "
                                    + destDir, 
                                  Repository.ObservableActionEnum.NEW_FOLDER,
                                  destDir));
                }
            }
        });

        CollectionUtils.forAllDo(republishedFiles, new Closure() {
            public void execute(final Object data) {
                final Material material = (Material)data;
                final File destFile = getDestDir(course, material);
                
                republishStrategy.republish(material, destFile);
                
                try {
                    final InputStream in = material.getInputStream();
                    logger.info("Descargando: " + material.getName());
                    copyInput(in, destFile, material.getEstimatedSize());
                    in.close();
                    setChanged();
                    notifyObservers(
                            new Repository.ObservableAction(
                                    "material didáctico republicado"
                                    + destFile, 
                              Repository.ObservableActionEnum.REPUBLISHED_FILE,
                              destFile));
                } catch(IOException e) {
                    logger.error("saving material", e);
                    throw new RuntimeException(e);
                }
            }
        });
        
        CollectionUtils.forAllDo(newFiles, new Closure() {
            private int errors = 0; 
            public void execute(final Object data) {
                final Material material = (Material)data;
                final File destFile = getDestDir(course, material);
                if(destFile != null) {
                    FilesystemUtils.mkdir(destFile.getParentFile());
                    logger.debug("getting " + destFile);
                    
                    try {
                        InputStream in = material.getInputStream();
                        logger.info("Descargando: " + material.getName());
                        copyInput(in, destFile, material.getEstimatedSize());
                        in.close();
                        setChanged();
                        notifyObservers(
                                new Repository.ObservableAction(
                                        "nuevo material didáctico "
                                        + destFile, 
                                  Repository.ObservableActionEnum.NEW_FILE,
                                  destFile));
                    } catch(IOException e) {
                        errors++;
                        logger.error("Error al guardar material "
                                + e.getLocalizedMessage(), e);
                        if(errors > 5) {
                            logger.error("Abortando. Demasiados errores."
                                    + e.getLocalizedMessage(), e);
                            throw new RuntimeException(e);
                            
                        }
                    }
                }
            }
        });
    }
    
    /** @see #addRepositoryListener(Observer) */
    public final void addRepositoryListener(final Observer observer) {
        addObserver(observer);
    }
    
    /** @see Repository#setRepositoryListeners(java.util.List) */
    public final void setRepositoryListeners(final List observers) {
        ListUtils.predicatedList(observers, new Predicate() {
            public boolean evaluate(final Object arg0) {
                return arg0 instanceof Observer;
            }
        });
        CollectionUtils.forAllDo(observers, new Closure() {
            public void execute(final Object arg0) {
                addRepositoryListener((Observer)arg0);
            }
        });
    }

    /**
     * Resuelve el directorio donde debe quedar almacenado un curso
     *  
     * @param course curso
     * @return un directorio  donde debe quedar almacenado un curso
     */
    final File getDestDir(final Course course) {
        final String name = course.getName();
        if(name.indexOf('/') != -1) {
            throw new IllegalStateException("la materia `" + name
                    + "`tiene caracteres inseguros");
        }
        
        return new File(base + File.separator +  StringUtils.upperCase(name)
                + File.separator + "material");
    }
    
    /**
     * @param course curso en cuestión
     * @param material material a almacenar
     * 
     * @return un path a donde debe quedar almacenado un archivo de un curso, o
     *          <code>null</code> si es inseguro.
     */
    private File getDestDir(final Course course, final Material material) {
        String name = material.getName();
         File ret;
         
        if(relativePathValidator.isValid(name)) {
            ret = new File(getDestDir(course), name);
        } else {
            ret = null;
        }
         
         return ret;
    }
    
    /**
     * 
     * copia el conteido de source a destination
     * 
     * @param source source stream
     * @param destination destination stream
     * @param size tamaño estimado del contenido a copiar
     * @throws IOException on error
     */
    private void copyInput(final InputStream source, final File destination,
            final long size) throws IOException {
        final int oneKB = 1024;
        final byte[] buffer = new byte[1 * oneKB];
        int length, total = 0;
        boolean clean = true;
        
        OutputStream out = null;
        FilesystemUtils.mkdir(destination.getParentFile());
        File tmpDestination = File.createTempFile("download", "tmp",
                destination.getParentFile());
        try {
            out = new FileOutputStream(tmpDestination);
            meter.update(total, (int)size);
            while ((length = source.read(buffer)) >= 0) {
                out.write(buffer, 0, length);
                total += length;
            }
            meter.finish(total);
        } catch (Throwable t) {
            clean = false;
            throw new RuntimeException(t);
        } finally {
            if(clean) {
                out.close();
                tmpDestination.renameTo(destination);
            }
        }
    }
}