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

import javax.swing.JComponent;
import javax.swing.JTextArea;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Appender de log4j, que loguea el contenido en textarea.
 * 
 * @author Juan F. Codagnone
 * @since Mar 27, 2005
 */
public class JTextAreaAppender extends AppenderSkeleton {
    /** visual component */
    private final JTextArea area = new JTextArea();
    /** layout log4j layout to use (injected) */
    private final Layout layout;
    /** number of rows to show */
    private static final int NUMBER_OF_ROWS = 10;
    /**
     * Crea el JTextAreaAppend.
     * @param layout log4j layout to use
     */
    public JTextAreaAppender(final Layout layout) {
        this.layout = layout;
        area.setRows(NUMBER_OF_ROWS);
        area.setEditable(false);
        area.setLineWrap(true);
    }
    
    /**
     * @return the visual component
     */
    public final JComponent asJComponent() {
        return area;
    }
    
    /** @see AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent) */
    protected final void append(final LoggingEvent event) {
        area.append(layout.format(event));
    }

    /** @see org.apache.log4j.Appender#close() */
    public final void close() {
        // nothing to do
    }

    /** @see org.apache.log4j.Appender#requiresLayout() */
    public final  boolean requiresLayout() {
        return false;
    }
}
