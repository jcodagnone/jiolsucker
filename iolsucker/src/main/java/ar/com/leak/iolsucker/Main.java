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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import ar.com.leak.iolsucker.container.Configurator;
import ar.com.leak.iolsucker.container.swing.JCrashDialog;
import ar.com.leak.iolsucker.model.impl.common.login.WrongCredentials;


/**
 * TODO Brief description.
 *
 * TODO Detail
 *
 * @author Juan F. Codagnone
 * @since Aug 4, 2005
 */
public class Main {
    /** class logger */
    private final  Log logger = LogFactory.getLog(Main.class);

    /**
     * Program entry point
     * @param args command line arguments
     * @throws Exception on error
     */
    public static void main(final String [] args) throws Exception {
        new Main().run(new ClassPathResource("spring/default.xml"), args);
    }
    
    /**
     * Corre el programa: instancia todo lo necesario, y despues corre los 
     * controladores
     * 
     * <p>
     * Con un parámetro de linea de comando se puede un xml que actue de 
     * bean factory y componga la ejecución
     * </p>
     * <p>
     *  Con la propiedad del sistema <code>iolsucker.factory.options</code> se
     *  puede especificar una clase de tipo <code>OptionsDAO</code> para 
     *  determinar como se almacenan las opciones
     * </p>
     * @throws Exception
     * @see PicoContainerFactory
     */
    public final void run(final AbstractResource defaultResource,
            final String []args) throws ClassNotFoundException {
        XmlBeanFactory factory = null;
        
        if(args.length != 0) {
            try {
                factory = new XmlBeanFactory(new FileSystemResource(args[0]));
            } catch(Throwable e) {
                factory = null;
                e.printStackTrace();
            }
        } else {
            try {
                factory = new XmlBeanFactory(defaultResource);
            } catch(final RuntimeException e) {
                if(e.getCause() != null 
                   && e.getCause() instanceof FileNotFoundException) {
                    // this is ok and expected
                    e.printStackTrace();
                } else {
                    new JCrashDialog(e, null);
                }
            }
        }
        
        if(factory == null) {
            System.out.println("\nUsage: " + Main.class.getName() 
                    + " [/path/to/configuration.xml]\n"
                    + "If you don't pass any parameter, it tries to load the "
                );
        } else {
            
            try {
                final Configurator configurator = 
                    (Configurator)factory.getBean("configurator");
                
                final XmlBeanFactory  aFactory = factory;
                Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                    public void run() {
                        // XXX dirty hack: en windows, la implemetacion de
                        // swing es mala, y si de apender existe un 
                        // JTextAreaAppender, la aplicación se queda trabada
                        // por siempre.
                        Logger.getRootLogger().removeAllAppenders();
                        aFactory.destroySingletons();
                    }
                }));
                
                final List<Runnable> runnables = configurator.filterControllers(
                        new ArrayList<Runnable>(
                              factory.getBeansOfType(Runnable.class).values()));
                for(final Runnable r : runnables) {
                    r.run();
                }
                factory.destroySingletons();
            } catch(RuntimeException  e) {
                Throwable cause = e;
                for(  ; cause.getCause() != null; cause = cause.getCause()) {
                    // void
                }
                if(cause instanceof WrongCredentials) {
                    logger.fatal(cause.getMessage());
                    logger.debug("causa:");
                    logger.debug(e);
                } else {
                    logger.fatal("error ejecutando jiolsucker", e);
                    new JCrashDialog(e, null);
                }
            } catch(Exception e) {
                Throwable cause = e;
                for(  ; cause.getCause() != null; cause = cause.getCause()) {
                    // void
                }
                logger.error(cause.getMessage());
            } catch(Throwable e) {
                logger.fatal("error ejecutando jiolsucker", e);
                new JCrashDialog(e, null);
            }
        }
    }
}
