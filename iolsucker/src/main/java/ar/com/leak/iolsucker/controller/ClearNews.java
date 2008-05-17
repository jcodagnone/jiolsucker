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


import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.com.leak.iolsucker.model.IolDAO;
import ar.com.leak.iolsucker.model.News;

/**
 * 
 * Marca como leidas a todas las noticias de IOL
 * 
 * @author Juan F. Codagnone
 * @since Jul 5, 2005
 */
public class ClearNews implements Runnable {
    /** class logger */
    private final Log logger = LogFactory.getLog(ClearNews.class);
    /** dao a usar */
    private final IolDAO dao;
    
    /**
     * Crea el ClearNews.
     *
     * @param dao el dao usado por el controlador
     */
    public ClearNews(final IolDAO dao) {
        this.dao = dao;
    }
    
    /** @see Runnable#run() */
    public final void run() {
        CollectionUtils.forAllDo(dao.getUnreadNews(), new Closure() {
            public void execute(final Object object) {
                final News news = (News)object;
                logger.info("removing unread news: " + news);
                news.markAsReaded();
            };
        });        
    }
}
