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
package ar.com.leak.iolsucker.controller;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.leak.iolsucker.model.IolDAO;

/**
 * Controlador que solamente chequea las noticias
 * 
 * @author Juan F. Codagnone
 * @since Apr 26, 2005
 */
public class News implements Runnable {
    /** class logger */
    private final Logger logger = LoggerFactory.getLogger(News.class);
    /** mostrar la cantidad de noticias totales? */
    private final boolean bAll;
    /** dao a utilizar para obtener las noticias (inyectado) */
    private final IolDAO dao;
    
    /**
     * Crea el News.
     *
     * @param dao dao a utilizar para obtener las noticias
     * @param bAll <code>true</code> si se quiere obtener la cantidad de 
     *           noticias totales  (puede ser que tarde más)
     */
    public News(final IolDAO dao, final boolean bAll) {
        this.dao = dao;
        this.bAll = bAll;
    }
    
    /** @see Runnable#run() */
    public final void run() {
        final Collection unread = dao.getUnreadNews();
        final String s = unread.size() != 1 ? "s" : "";
        final String total = bAll ? " de un total de " 
                + dao.getNews().size() : ".";
        
        logger.info("Usted tiene " + unread.size() + " noticia" + s 
                + " nueva" + s + " sin leer" + total);
    }
}
