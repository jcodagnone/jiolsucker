/*
 * Copyright 2005 Juan F. Codagnone <juam at users dot sourceforge dot net>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ar.com.leak.iolsucker.impl.http;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import org.apache.commons.lang.Validate;

import ar.com.leak.iolsucker.model.Material;

/**
 * Creador de materiales para iol http.
 * 
 * @author Juan F. Codagnone
 * @since Feb 25, 2005
 * @depend - crea - ar.com.leak.iolsucker.model.Material
 * @depend - crea - ar.com.leak.iolsucker.impl.http.HTTPClient
 */
public class HTTPMaterialFactory {
    /** factory para crear nuevos requests */
    private final HTTPRequestFactory requestFactory;
    /** cantidad de trabajadores en paralelo para la recolección de directorios
     *  y archivos
     */
    private final int nWorkers;
    
    /**
     * Crea el HTTPMaterialFactory.
     *
     * @param nWorkers cantidad de trabajadores en paralelos para la adquisión
     *                 de datos (archivos y directorios de la materia)
     * @param requestFactory request factory
     */
    public HTTPMaterialFactory(final int nWorkers, 
            final HTTPRequestFactory requestFactory) {
        Validate.isTrue(nWorkers > 0, "debe haber por lo menos un trabajador");
        Validate.notNull(requestFactory, "requestFactory");
        
        this.requestFactory = requestFactory;
        this.nWorkers = nWorkers;
    }

    /**
     * @param httpClient
     *            un cliente http
     * @return una collecci[on del
     *         <code><Material/code> con el material didáctico
     *         del contexto actual
     * @throws IOException si hay algún problema
     */
    public final Collection<Material> getMaterial(final HTTPClient httpClient)
            throws IOException {
        final Collection<Material> ret = new Vector<Material>();
        final String cmd = httpClient.getNamingMapper().getCourseFilesCommand();
        
        final BlockingQueue<Runnable> requests = 
            new LinkedBlockingQueue<Runnable>();
        requests.offer(requestFactory.createRequest(httpClient, cmd, "", cmd, 
                ret, requests));
        final Semaphore semaphore = new Semaphore(nWorkers);
        final CountDownLatch signal = new CountDownLatch(1);
        
        if(nWorkers == 1) {
            while(requests.size() != 0) {
                requests.poll().run();
            }
        } else {
            final Thread[] threads = new Thread[nWorkers];
            final Runnable r = new Runnable() {
                public void run() {
                    boolean run = true;
                    
                    while(run) {
                        try {
                            final Runnable r = requests.take();
                            semaphore.acquireUninterruptibly();
                            signal.countDown();
                            r.run();
                            semaphore.release();
                        } catch(final Throwable e) {
                            run = false;
                        }
                    }
                }
            };
            
            for(int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(r);
                threads[i].start();
            }
            
            try {
                signal.await();
                semaphore.acquireUninterruptibly(nWorkers);
            } catch(final InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                for(int i = 0; i < threads.length; i++) {
                    threads[i].stop();
                }
            }
        }
        
       return ret;
    }
}
