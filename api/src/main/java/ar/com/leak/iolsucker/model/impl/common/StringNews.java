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
package ar.com.leak.iolsucker.model.impl.common;

import org.apache.commons.lang.NotImplementedException;

import ar.com.leak.iolsucker.model.News;

/**
 * 
 * @author Juan F. Codagnone
 * @since Apr 26, 2005
 *
 */
public class StringNews implements News {
    /** titulo de la noticia */
    private final String title;
    /** contenido de la noticia*/
    private final String body;
    
    /**
     * Crea el StringNews.
     *
     * @param title titulo de la noticia
     * @param body contenido de la noticia
     */
    public StringNews(final String title, final String body) {
        this.title = title;
        this.body = body;
    }
    
    /** @see News#getTitle() */
    public final String getTitle() {
        return title;
    }

    /** @see News#getBody() */
    public final String getBody() {
        return body;
    }

    /** @see News#markAsReaded() */
    public final void markAsReaded() {
        throw new NotImplementedException("");
    }
}
