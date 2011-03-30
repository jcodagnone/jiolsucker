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
package ar.com.leak.iolsucker.container.swing;

import javax.swing.*;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.Filter;

import ar.com.leak.iolsucker.view.common.JTextAreaAppender;

/**
 * TODO Agregar comentarios pertinentes
 *
 * @author Juan F. Codagnone
 * @since Apr 12, 2005
 */
public class ProgressView extends Box {
    /**
     * Creates the ProgressView.
     *
     * @param layout layout log4j
     * @param filter filtro log4j
     * @param action que hace el boton de la vista
     */
    public ProgressView(final Layout layout, final Filter filter,
            final Action action) {
        super(BoxLayout.Y_AXIS);
        JTextAreaAppender appender = new JTextAreaAppender(layout);
        appender.addFilter(filter);
        Logger.getRootLogger().addAppender(appender);
        JScrollPane pane = new JScrollPane(appender.asJComponent());
        pane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(pane);
        this.setBorder(new TitledBorder(new EtchedBorder(), "Progreso"));
        final JButton btnCancel = new JButton("Cancel");
        btnCancel.setAction(action);
        final Box h = Box.createHorizontalBox();
        h.add(btnCancel);
        this.add(h);
    }
}