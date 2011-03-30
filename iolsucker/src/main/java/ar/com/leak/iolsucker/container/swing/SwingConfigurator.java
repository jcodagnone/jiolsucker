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
package ar.com.leak.iolsucker.container.swing;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.leak.iolsucker.ProjectInfo;
import ar.com.leak.iolsucker.container.Configurator;
import ar.com.leak.iolsucker.container.Options;
import ar.com.leak.iolsucker.container.OptionsDAO;
import ar.com.leak.iolsucker.container.swing.OptionsView.Status;
import ar.com.leak.iolsucker.controller.ClearNews;
import ar.com.leak.iolsucker.controller.Iolsucker;
import ar.com.leak.iolsucker.controller.News;


/**
 * TODO Brief description.
 *
 * TODO Detail
 *
 * @author Juan F. Codagnone
 * @since Aug 4, 2005
 */
public class SwingConfigurator implements Configurator {
    /** logger... */
    private final  Logger logger = LoggerFactory.getLogger(SwingConfigurator.class);
    /** holds the optionsDao... */
    private final OptionsView optionsView;

    /**
     * Creates the SwingConfigurator.
     *
     * @param optionsDAO opciones
     */
    public SwingConfigurator(final OptionsDAO optionsDAO) {
        final JFrame dialog = new JFrame();

        final ProjectInfo info = new ProjectInfo();
        final Options options;
        try {
            options = optionsDAO.getOptions();
        } catch(Exception e1) {
            throw new RuntimeException(e1);
        }
        optionsView = new OptionsView(dialog, options, info);

        final Status status;
        if(options.isSaveData() && options.isSavePassword()
                && options.isStartWithoutPrompting()) {
            status = Status.OK;
        } else {
            dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            dialog.addWindowListener(new WhenClosingTheWindow());
            dialog.setTitle(info.getProjectName() + " v"
                    + info.getProjectVersion());
            dialog.getContentPane().add(optionsView.asComponent());
            dialog.pack();
            UiUtil.centerAndShow(dialog);

            waitForWindowToGetClosed();

            status = optionsView.getStatus();
        }
        final String proxyHost = options.getProxyHost();
        final String proxyPort = Integer.toString(options.getProxyPort());
        if(proxyHost.trim().length() != 0) {
            logger.debug("setting proxy to: " + proxyHost + ": "
                    + proxyPort);
            System.setProperty("http.proxyHost", proxyHost);
            System.setProperty("http.proxyPort", proxyPort);
        }

        if(status == Status.OK) {
            try {
                if(options.isSaveData()) {
                    optionsDAO.saveOptions(options);
                } else {
                    optionsDAO.clearOptions();
                }
            } catch(Exception e) {
                logger.error("almacenando las opciones", e);
            }
            JFrame frame = new JFrameProgressView(
                "jiol Sucker",
                options,
                new PatternLayout(optionsView.getVerboseEnum().getPattern()),
                optionsView.getVerboseEnum().getFilter(),
                new AbstractAction() {
                    {
                        putValue(AbstractAction.NAME, "Salir");
                    }
                    public void actionPerformed(final ActionEvent ae) {
                        try {
                            optionsDAO.saveOptions(options);
                        } catch(Exception e) {
                            logger.error("almacenando las opciones", e);
                            System.exit(1);
                        }
                        System.exit(0);
                    }
                });
            frame.pack();
            UiUtil.centerAndShow(frame);
        } else if(status == Status.CANCEL) {
            System.exit(0);
        } else {
            throw new AssertionError("status no esperado");
        }
    }

    /**
     * This method makes the current thread to sleep untill it is awaken by the
     * windowClosed event
     *
     * @see SwingConfigurator.WhenClosingTheWindow
     */
    private void waitForWindowToGetClosed() {
        try {
            synchronized (this) {
                this.wait();
            }
        } catch (InterruptedException e) {
            logger.warn("SwingConfigurator's thread interrupted", e);
        }
    }

    /**
     * @see Configurator#filterControllers(java.awt.List)
     */
    @SuppressWarnings("unchecked")
    public final List<Runnable> filterControllers(
            final List<Runnable> controllers) {
        return new ArrayList<Runnable>(CollectionUtils.select(
                controllers, new Predicate() {
            public boolean evaluate(final Object arg0) {
                final Runnable controller = (Runnable)arg0;
                return
                  ((controller instanceof Iolsucker && optionsView.syncFiles())
                  || (controller instanceof News && optionsView.syncNews())
                  || (controller instanceof ClearNews
                          && optionsView.clearNews()));
            }
        }));
    }

    /** @return el modelo de datos */
    public final Options getOptions() {
        return optionsView.getModel();
    }

    /**
     * This class awakes the threads that waits for the window
     * to be closed.
     * @see SwingConfigurator#waitForWindowToGetClosed()
     * @author Eduardo Pereda IV
     */
    private class WhenClosingTheWindow extends WindowAdapter {
        /** @see WindowAdapter#windowClosed(java.awt.event.WindowEvent) */
        @Override
        public void windowClosed(final WindowEvent windowEvent) {
            synchronized (SwingConfigurator.this) {
                SwingConfigurator.this.notifyAll();
            }
        }
    }
}
