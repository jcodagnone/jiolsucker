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
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;

import ar.com.leak.iolsucker.view.Repository;
import ar.com.leak.iolsucker.view.Repository.ObservableAction;


/**
 * Changelog de lo que pasa en el repositorio en formato txt
 *
 * @author Juan F. Codagnone
 * @since Aug 18, 2005
 */
public class TextChangelogObserver implements Observer {
    /** an appender */
    private final Appender appender;
    
    /**
     * Creates the TextChangelogObserver.
     *
     * @param outputFile cangelog filename
     * @throws IOException on error
     */
    public TextChangelogObserver(final File outputFile) throws IOException {
        appender = new RollingFileAppender(
                new PatternLayout("%d{HH:mm:ss} - %m%n"),
                outputFile.getAbsolutePath(), true);
    }

    /** @see java.util.Observer#update(Observable, Object) */
    public final void update(final Observable o, final Object arg) {
        final Repository.ObservableAction action = (ObservableAction)arg;
        appender.doAppend(new LoggingEvent(
                TextChangelogObserver.class.getName(),
                Category.getInstance(TextChangelogObserver.class),
                Priority.INFO, action.getMsg(), null));
        
    }

    /** free all the resourses */
    public final void dispose() {
        appender.close();
    }
}
