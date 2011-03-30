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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

import ar.com.leak.common.swing.GridBagConstraintsHelper;
import ar.com.leak.common.swing.GridBagHelper;
import ar.com.leak.iolsucker.ProjectInfo;
import ar.com.leak.iolsucker.container.Options;

/**
 *
 * Vista swing de Options
 *
 * @see ar.com.leak.iolsucker.container.Options
 * @author Juan F. Codagnone
 * @since Apr 12, 2005
 */
public class OptionsView  {
    /** panel padre */
    private final GridBagHelper panel = new GridBagHelper();
    /** boton de ok */
    private final JButton btnOk = new JButton("Ok");
    /** boton de cancel */
    private final JButton btnCancel = new JButton("Cancel");
    /** modelo de la vista */
    private final Options model;
    /** about dialog */
    private final AboutDialog aboutDialog;
    /** tamaño de los márgenes de la ventana. */
    private static final int TOP_MARGIN = 10, LEFT_MARGIN = 10,
            BOTTOM_MARGIN = 10, RIGHT_MARGIN = 10;
    /** tamaños de los márgenes de los paneles internos */
    private static final int TOP_MARGIN_INNER_WND = 5,
            LEFT_MARGIN_INNER_WND = 5,
            BOTTOM_MARGIN_INNER_WND = 5,
            RIGHT_MARGIN_INNER_WND = 5;

    /**
     *
     * Creates the OptionsView.
     *
     * @param parent parent dialog (puede ser null?)
     * @param model modelo de datos para la vista
     * @param info información del programa
     */
    public OptionsView(final Frame parent, final Options model,
            final ProjectInfo info) {
        Validate.notNull(model, "model");
        Validate.notNull(info, "info");

        this.model = model;
        aboutDialog = new AboutDialog(parent, info);
        aboutDialog.pack();

        panel.asJComponent().setBorder(
                BorderFactory.createEmptyBorder(TOP_MARGIN, LEFT_MARGIN,
                        BOTTOM_MARGIN, RIGHT_MARGIN));

        panel.getConstraints().gridx = 0;
        panel.getConstraints().fill = GridBagConstraints.BOTH;
        panel.getConstraints().weightx = 1;
        panel.getConstraints().anchor = GridBagConstraints.CENTER;

        panel.getConstraints().weighty = 1;
        panel.add(getLoginPanel());

        panel.getConstraints().weighty = 0;
        panel.add(getControllersPanel());

        panel.getConstraints().weighty = 1;
        panel.add(getAdvancedPanel());

        panel.getConstraints().weighty = 0;
        panel.add(getSavePanel());

        panel.getConstraints().weighty = 0;
        panel.add(getBotonera(parent));

        final int panelWidth = 480;
        final int panelHeight = 570;
        panel.asJComponent().setPreferredSize(
                new Dimension(panelWidth, panelHeight));

        enableOk();
    }

    /** @return un awt component z*/
    public final Component asComponent() {
        return panel.asJComponent();
    }

    /** @return el modelo que representa esta vista */
    public final Options getModel() {
        return model;
    }

    /**
     * @param parent parent dialog
     * @return la botonera con los botones
     */
    private JComponent getBotonera(final Frame parent) {
        final int numRows = 1;
        final int numCols = 3;
        final int hGap = 8;
        final int vGap = 1;
        JPanel botonera = new JPanel(new GridLayout(numRows, numCols, hGap,
                vGap));
        GridBagHelper botoneraContainer = new GridBagHelper();
        botoneraContainer.getConstraints().insets = new Insets(2, 2, 2, 2);
        botoneraContainer.getConstraints().weightx = 1;

        final int minButtonWidth = 30;
        botoneraContainer.getConstraints().ipadx = minButtonWidth;
        botoneraContainer.add(botonera);

        botonera.add(btnOk);
        botonera.add(btnCancel);

        Action okAction = new AbstractAction() {
            { putValue(AbstractAction.NAME, "Ok"); }
            public void actionPerformed(final ActionEvent e) {
                status = Status.OK;
                parent.dispose();
            }
        };
        Action cancelAction = new AbstractAction() {
          { putValue(AbstractAction.NAME, "Cancel"); }
          public void actionPerformed(final ActionEvent e) {
              status = Status.CANCEL;
              parent.dispose();
          }
        };
        btnOk.setAction(okAction);
        btnCancel.setAction(cancelAction);

        JButton button = new JButton("Sobre...");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                UiUtil.centerOnParentAndShow(aboutDialog);
            }
        });
        botonera.add(button);

        return botoneraContainer.asJComponent();
    }

    /**
     * @return un panel de configuración con todos los datos de configuración
     */
    private JComponent getLoginPanel() {
        final GridBagHelper login = new GridBagHelper();

        final GridBagConstraints leftColConstraint = GridBagConstraintsHelper
                .getColumnConstraint(0, GridBagConstraints.WEST, 0,
                        GridBagConstraints.HORIZONTAL, 2);
        final GridBagConstraints righColConstraint = GridBagConstraintsHelper
        .getColumnConstraint(1, GridBagConstraints.EAST, 1,
                GridBagConstraints.HORIZONTAL, 2);

        final JTextField username = new JTextField();
        login.add(new JLabel("Usuario"), leftColConstraint);
        username.setToolTipText("* usuario con el cual se ingresará a IOL."
                + " Ej: 29123321");
        username.setText(model.getUsername());
        username.getDocument().addDocumentListener(new MyDocumentAdapter(model,
                username, false));
        login.add(username, righColConstraint);

        final JTextField password = new JPasswordField();
        login.add(new JLabel("Contraseña"), leftColConstraint);
        login.add(password, righColConstraint);
        password.setText(model.getPassword());
        password.setToolTipText("* password del usuario.");
        password.getDocument().addDocumentListener(new MyDocumentAdapter(model,
                password, true));
        login.add(new JLabel("Repositorio"), leftColConstraint);

        final FileChosser repo = new FileChosser();
        repo.setSelected(model.getRepository());
        repo.setToolTipText("* directorio donde se guardan los archivos");
        login.add(repo, righColConstraint);
        repo.addListener(new ChangeListener() {
            public void stateChanged(final ChangeEvent e) {
                model.setRepository(repo.getSelected());
                enableOk();
            }
        });

        login.asJComponent().setBorder(
                BorderFactory.createCompoundBorder(new TitledBorder(
                        new EtchedBorder(), "Login"), BorderFactory
                        .createEmptyBorder(TOP_MARGIN_INNER_WND,
                                LEFT_MARGIN_INNER_WND,
                                BOTTOM_MARGIN_INNER_WND,
                                RIGHT_MARGIN_INNER_WND)));

        return login.asJComponent();
    }
    /** edit que guarda la url donde está IOL */
    private final JTextField edtURL = new JTextField();
    /** listener para el campo de la url de IOL */
    private URLDocumentListener urlListener;
    /** combo para elegir el nivel de verborragia */
    private final JComboBox verbosityCombo = new JComboBox(
            VerbosityEnum.asList().toArray());

    /** @return el panel de opciones avanzadas */
    private JComponent getAdvancedPanel() {
        final JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder(new EtchedBorder(), "Opciones Avanzadas"),
                BorderFactory.createEmptyBorder(TOP_MARGIN_INNER_WND,
                        LEFT_MARGIN_INNER_WND, BOTTOM_MARGIN_INNER_WND,
                        RIGHT_MARGIN_INNER_WND)));

        final GridBagLayout gridBag = new GridBagLayout();
        mainPanel.setLayout(gridBag);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 0, 0, 0);


        final GridBagHelper aPanel = new GridBagHelper();
        final GridBagConstraints leftColumn = GridBagConstraintsHelper
                .getColumnConstraint(0, GridBagConstraints.WEST, 0,
                        GridBagConstraints.HORIZONTAL, 2);
        final GridBagConstraints rightColumn = GridBagConstraintsHelper
                .getColumnConstraint(1, GridBagConstraints.EAST, 1,
                        GridBagConstraints.HORIZONTAL, 2);

        aPanel.add(new JLabel("URL Base de IOL"), leftColumn);
        aPanel.add(edtURL, rightColumn);
        edtURL.setText(model.getURLBase().toString());
        edtURL.setToolTipText("dirección base de ITBA-OnLine. Puede "
                + "ser util cambiarlo, si alguien le prohibe el acceso por "
                + "nombre, no puede resolver el nombre, o tunelea via ssh");

        urlListener = new URLDocumentListener(edtURL, model);
        edtURL.getDocument().addDocumentListener(urlListener);

        aPanel.add(new JLabel("Proxy"), leftColumn);
        GridBagHelper box = new GridBagHelper();
        box.getConstraints().fill = GridBagConstraints.BOTH;
        box.getConstraints().weightx = 1;
        box.getConstraints().weighty = 1;

        final JTextField proxyHost = new JTextField();
        proxyHost.setText(model.getProxyHost());
        proxyHost.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(final DocumentEvent e) {
                changed();
            }

            public void removeUpdate(final DocumentEvent e) {
                changed();
            }

            public void changedUpdate(final DocumentEvent e) {
                changed();
            }

            private void changed() {
                model.setProxyHost(proxyHost.getText());
            }
        });
        box.add(proxyHost);
        final SpinnerNumberModel portModel = new SpinnerNumberModel(model
                .getProxyPort(), 1, 0xFFFF, 1);
        final JSpinner spinner = new JSpinner(portModel);
        spinner.setToolTipText("puerto del proxy. entero entre 1 y 0xFFFF");
        portModel.addChangeListener(new ChangeListener() {
            public void stateChanged(final ChangeEvent e) {
                model.setProxyPort(portModel.getNumber().intValue());
            }
        });
        box.getConstraints().weightx = 0;
        box.add(spinner);
        aPanel.add(box.asJComponent(), rightColumn);

        aPanel.add(new JLabel("Verborragia"), leftColumn);
        verbosityCombo.setSelectedItem(verbosityCombo);
        verbosityCombo.addItemListener(new ItemListener() {

            public void itemStateChanged(final ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    VerbosityEnum level = (VerbosityEnum)e.getItem();
                    level.set();

                }
            }

        });
        aPanel.add(verbosityCombo, rightColumn);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.weightx = 1;
        constraints.weighty = 1;
        gridBag.setConstraints(aPanel.asJComponent(), constraints);
        mainPanel.add(aPanel.asJComponent());

        final JCheckBox chkFilter = new JCheckBox(
                "Elegir que materias sincronizar (luego se seleccionan)");
        chkFilter.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                model.setFilterCourses(chkFilter.isSelected());
            }
        });
        chkFilter.setSelected(model.isFilterCourses());
        constraints.gridx = 0;
        constraints.gridy = 1;
        gridBag.setConstraints(chkFilter, constraints);
        mainPanel.add(chkFilter);

        return mainPanel;
    }

    /** estado de la vista */
    private Status status = Status.UNKNOWN;

    /** @return el estado de la vista */
    public final Status getStatus() {
        return status;
    }

    /**
     * Estado del modelo
     *
     * @author Juan F. Codagnone
     */
    public static final class Status {
        /** a name */
        private final String name;

        /**
         * Creates the Status.
         *
         * @param name un nombre
         */
        private Status(final String name) {
            this.name = name;
        }

        /** estado desconocido */
        public static final Status UNKNOWN = new Status("unknown");
        /** estado ok */
        public static final Status OK = new Status("ok");
        /** estado cancel */
        public static final Status CANCEL = new Status("cancel");

        /** @see Object#toString() */
        public String toString() {
            return name;
        }
    }

    // ////////////////////////////////////////////////////////////////////////
    /** checkbox para seleccionar si se corre el controlador de sync de
     * archivos */
    private JCheckBox chkSyncArchivos = new JCheckBox(
            "Sincronizar archivos");
    /** checkbox para seleccionar si se corre el controlador de sync de
     * noticias*/
    private JCheckBox chkSyncNoticias =
        new JCheckBox("Chequear noticias");
    /** checkbox para seleccionar si se corre el controlador de limpieza de
     * noticias */
    private JCheckBox chkClearNoticias = new JCheckBox(
            "Borrar todas las noticias");

    /** @return el panel donde estan todos los checkboxs para elegir que
     * controladores se corren */
    private JComponent getControllersPanel() {
        final GridBagHelper aPanel = new GridBagHelper();

        aPanel.getConstraints().weightx = 1;

        // TODO almacenar opciones, obtener controladores de algun lado,
        // configrar el controlador etc.
        chkSyncArchivos.setSelected(true);
        chkSyncNoticias.setSelected(true);
        chkClearNoticias.setSelected(false);
        aPanel.add(chkSyncArchivos);
        aPanel.add(chkSyncNoticias);
        aPanel.add(chkClearNoticias);
        aPanel.asJComponent().setBorder(
                BorderFactory.createCompoundBorder(new TitledBorder(
                        new EtchedBorder(), "Controladores"), BorderFactory
                        .createEmptyBorder(TOP_MARGIN_INNER_WND,
                                LEFT_MARGIN_INNER_WND, BOTTOM_MARGIN_INNER_WND,
                                RIGHT_MARGIN_INNER_WND)));

        return aPanel.asJComponent();

    }

    /** @return <code>true</code> si se debe correr el daemon sync de archivos*/
    public final boolean syncFiles() {
        return chkSyncArchivos.isSelected();
    }

    /** @return <code>true</code> si se debe correr el daemon sync de noticias*/
    public final  boolean syncNews() {
        return chkSyncNoticias.isSelected();
    }

    /** @return <code>true</code> si se debe correr el daemon limpiador de
     * noticias*/
    public final boolean clearNews() {
        return chkClearNoticias.isSelected();
    }

    // /////////////////////////////////////////////////////////////////////////

    /** @return la verborragia seleccionada en la gui */
    public final VerbosityEnum getVerboseEnum() {
        return (VerbosityEnum)verbosityCombo.getSelectedItem();
    }

    /** @return el panel con las opciones para guardar datos y passwords */
    private JComponent getSavePanel() {
        final JPanel aPanel = new JPanel();

        final GridBagLayout gridBag = new GridBagLayout();
        aPanel.setLayout(gridBag);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 0, 0, 0);

        final JLabel msg = new JLabel(
                "<html><font color=#ff0000>Advertencia:</font> "
                  + "Si bien la contraseña se codifica cuando se almacena, \n"
                  + "es posible que un usuario malo pueda acceder a ella. "
                  + " Si comparte la computadora con personas que no son de su "
                  + "confianza, no habilite la opción</html>");
        aPanel.setBorder(BorderFactory.createCompoundBorder(new TitledBorder(
                new EtchedBorder(), "Configuración"), BorderFactory
                .createEmptyBorder(TOP_MARGIN_INNER_WND, LEFT_MARGIN_INNER_WND,
                        BOTTOM_MARGIN_INNER_WND, RIGHT_MARGIN_INNER_WND)));

        final JCheckBox saveData = new JCheckBox(
                "Guardar la configuración, para poder reusarla más tarde");
        final JCheckBox startWithoutPrompting = new JCheckBox(
                "Iniciar la descarga de archivos sin preguntar");
        final JCheckBox savePassword = new JCheckBox(
                "Almacenar tambien la contraseña");

        saveData.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                boolean b = saveData.isSelected();
                savePassword.setEnabled(b);
                startWithoutPrompting.setEnabled(b
                        && savePassword.isSelected());
                msg.setEnabled(b);
                model.setSaveData(b);
            }
        });

        startWithoutPrompting.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                model.setStartWithoutPrompting(startWithoutPrompting
                        .isSelected());
            }
        });

        savePassword.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                boolean b = savePassword.isSelected();
                startWithoutPrompting.setEnabled(b && saveData.isSelected());
                model.setSavePassword(b);
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.weightx = 1d;
        constraints.weighty = 0d;

        constraints.gridy = 0;
        gridBag.setConstraints(startWithoutPrompting, constraints);
        aPanel.add(startWithoutPrompting);

        constraints.gridy++;
        gridBag.setConstraints(saveData, constraints);
        aPanel.add(saveData);

        constraints.gridy++;
        gridBag.setConstraints(savePassword, constraints);
        aPanel.add(savePassword);

        constraints.gridy++;
        gridBag.setConstraints(msg, constraints);
        aPanel.add(msg);

        boolean isSaveDat = model.isSaveData();
        boolean isSavePass = model.isSavePassword();
        boolean isStartWithoutPrompt = model.isStartWithoutPrompting();
        saveData.setSelected(isSaveDat);
        startWithoutPrompting.setSelected(isStartWithoutPrompt);
        if(isSaveDat) {
            savePassword.setSelected(isSavePass);
        }

        savePassword.setEnabled(isSaveDat);
        startWithoutPrompting.setEnabled(isSaveDat && isSavePass);

        return aPanel;
    }

    /** retorna true si se puede habilitar el boton ok */
    private void enableOk() {
        btnOk.setEnabled(model.isValid()
                && (urlListener == null ? true : urlListener.isValid()));
    }

    /**
     * Listener para sincronizar el user y la password de la vista, con el
     * modelo
     *
     * @author Juan F. Codagnone
     */
    private class MyDocumentAdapter implements DocumentListener {
        /** modelo */
        private final Options options;
        /** que estoy escuchando? */
        private final JTextField from;
        /** listener de password? */
        private final boolean password;

        /**
         * Creates the MyDocumentAdapter.
         *
         * @param options modelo de datos
         * @param from texfield que estoy escuchando
         * @param password el textfield contiene una password?
         */
        public MyDocumentAdapter(final Options options, final JTextField from,
                final boolean password) {
            this.options = options;
            this.from = from;
            this.password = password;
        }

        /** @see DocumentListener#changedUpdate(DocumentEvent) */
        public void changedUpdate(final DocumentEvent e) {
            changed();
        }

        /** @see DocumentListener#insertUpdate(DocumentEvent) */
        public void insertUpdate(final DocumentEvent e) {
            changed();

        }

        /** @see DocumentListener#removeUpdate(DocumentEvent) */
        public void removeUpdate(final DocumentEvent e) {
            changed();
        }

        /** la vista cambió */
        private void changed() {
            enableOk();
            if(password) {
                options.setPassword(from.getText());
            } else {
                options.setUsername(from.getText());
            }

        }
    }

    /**
     * Listener que verifica que la sintaxis de una URL que se ingresa, es
     * válida
     *
     * @author Juan F. Codagnone
     */
    class URLDocumentListener implements DocumentListener {
        /** color del fondo */
        private final Color bgcolor;
        /** widget de la vista*/
        private final JTextField edtURL;
        /** modelo de la vista */
        private final Options model;
        /** url que se escribe */
        private URL url;

        /**
         * Creates the URLDocumentListener.
         *
         * @param edtURL widget a escuchar
         * @param model modelo de datos de la vista
         */
        public URLDocumentListener(final JTextField edtURL,
                final Options model) {
            this.edtURL = edtURL;
            this.model = model;
            bgcolor = edtURL.getBackground();
            url = model.getURLBase();
        }

        /** @see DocumentListener#insertUpdate(DocumentEvent) */
        public void insertUpdate(final DocumentEvent e) {
            changed();
        }

        /** @see DocumentListener#removeUpdate(DocumentEvent) */
        public void removeUpdate(final DocumentEvent e) {
            changed();
        }

        /** @see DocumentListener#changedUpdate(DocumentEvent) */
        public void changedUpdate(final DocumentEvent e) {
            changed();
        }

        /** la vista cambió */
        private void changed() {
            String s = edtURL.getText();
            try {
                url = new URL(s);
                edtURL.setBackground(bgcolor);
                model.setURLBase(url);
            } catch(MalformedURLException e) {
                url = null;
                edtURL.setBackground(Color.RED);
            }
            enableOk();
        }

        /** @return <code>true</code> if the object is in a valid state */
        public boolean isValid() {
            return url != null;
        }
    }

    /**
     * Enumearción de niveles de verborragia
     *
     * @author Juan F. Codagnone
     */
    public static final class VerbosityEnum {
        /** enum entry's name */
        private final String name;
        /** enum entry's description */
        private final String description;
        /** enum entry's level */
        private final Level level;
        /** enum entry's pattern */
        private final String pattern;
        /** enum entry's filter */
        private final Filter filter;

        /** list with all the enum values */
        private static List<VerbosityEnum> verbosityEntries =
            new ArrayList<VerbosityEnum>();
        /** list with all the enum values */
        private static List<VerbosityEnum> unmodificableverbosityEntries = null;

        /**
         * Crea el VerbosityEnum.
         *
         * @param name nombre del nivel
         * @param descripion descripción del nivel
         * @param level nivel log4j asociado
         * @param pattern patron de log4j asociado
         * @param filter log4j filter
         */
        private VerbosityEnum(final String name, final String descripion,
                final Level level, final String pattern, final Filter filter) {
            this.name = name;
            this.description = descripion;
            this.level = level;
            this.pattern = pattern;
            this.filter = filter;
            verbosityEntries.add(this);
        }

        /** @return the description representation for the enum entry */
        public String getDescription() {
            return description;
        }

        /** @return the name representation for the enum entry */
        public String getName() {
            return name;
        }

        /** @see Object#toString() */
        public String toString() {
            return name;
        }

        /** @return the log4j pattern associated to this enum entry */
        public String getPattern() {
            return pattern;
        }

        /** @return the log4j filter associated to this enum entry */
        public Filter getFilter() {
            return filter;
        }

        /** @return una colección con todas los enums registrados. */
        public static List<VerbosityEnum> asList() {
            if(unmodificableverbosityEntries == null) {
                unmodificableverbosityEntries = Collections
                        .unmodifiableList(verbosityEntries);
                verbosityEntries = null; // free reference
            }

            return unmodificableverbosityEntries;
        }

        /**
         *  cambia la configuracion de log4j para poner las propiedades de este
         *  nivel.
         */
        public void set() {
            Category root = Logger.getRoot();
            Enumeration e = root.getAllAppenders();
            while(e.hasMoreElements()) {
                Appender appender = (Appender)e.nextElement();
                Layout layout = appender.getLayout();
                if(layout instanceof PatternLayout) {
                    ((PatternLayout)layout).setConversionPattern(pattern);
                }

            }
            root.setLevel(level);
        }

        /** patrón usado en el nivel Todos */
        private static String allPattern = "%c %-5p - %m%n";
        /** patrón simple */
        private static String simplePattern = "%m%n";

        /** filtro para log4j, que muestra solo mensajes de la aplicación */
        private static Filter leakFilter = new Filter() {
            public int decide(final LoggingEvent event) {
                return event.getLoggerName().
                     startsWith("ar.com.leak.iolsucker") ? ACCEPT : DENY;
            }
        };

        /** filtro para log4j, que muestra todos los mensajes */
        private static Filter allFilter = new Filter() {
            public int decide(final LoggingEvent event) {
                return ACCEPT;
            }
        };

        /** Normal: logeo de las cosas basicas */
        public static final VerbosityEnum NORMAL = new VerbosityEnum("Normal",
                "", Level.INFO, simplePattern, leakFilter);
        /** Debug: logeo de casi todas las cosas */
        public static final VerbosityEnum DEBUG = new VerbosityEnum(
                "Debug",
                "usado para ver que hacen los componentes cuando "
                + "algo no anda como se espera",
                Level.DEBUG, allPattern, leakFilter);
        /** Debug: logeo de todas las cosas */
        public static final VerbosityEnum ALL = new VerbosityEnum("Todos", "",
                Level.DEBUG, allPattern, allFilter);
    }

}


/**
 * Widget que se ve como un JTextField pero que permite seleccionar un archivo
 *
 * @author Juan F. Codagnone
 */
class FileChosser extends JPanel {
    /** text field a ver */
    private final JTextField field = new JTextField();

    /**
     * Crea el FileChosser.
     */
    public FileChosser() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        field.setEnabled(false);
        add(field);
        JButton button = new JButton("...");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                final JFileChooser chosser = new JFileChooser(field.getText());
                chosser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chosser.setDialogTitle("Elija su repositorio local");
                chosser.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        File file = chosser.getSelectedFile();
                        if(file != null) {
                            String path = file.getAbsolutePath();
                            change(path);
                        }
                    }
                });
                chosser.showDialog(null, null);
            }
        });
        add(button);
    }

    /** @return el archivo seleccionado */
    public final String getSelected() {
        return field.getText().trim();
    }

    /** cambia la selección
     * @param s nueva seleccion
     */
    public void setSelected(final String s) {
        if(s != null) {
            change(s);
        }
    }

    /** listeners invocados cuando algo (ej: cambia algo en la info de login) */
    private final Collection <ChangeListener>listeners =
        new ArrayList<ChangeListener>();

    /**
     * agrega un listener
     *
     * @param listener un listener
     */
    public final void addListener(final ChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * @param path nuevo path
     */
    protected void change(final String path) {
        field.setText(path);
        fireChanged();
    }

    /** notifica a los listeners, que algo ha cambiado */
    protected void fireChanged() {
        for(Iterator i = listeners.iterator(); i.hasNext();) {
            ChangeListener l = (ChangeListener)i.next();
            l.stateChanged(null);
        }
    }
}
