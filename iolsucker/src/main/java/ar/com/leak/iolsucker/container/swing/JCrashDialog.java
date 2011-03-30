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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.swing.*;

/**
 * <p>
 * La mayoría de las veces que ocurre un problema no esperado que deberia 
 * manejarse desde la programación, se muestra la causa en está dialog.
 * </p><p>
 * Muestra el contenido del stacktrace de algo <code>Throwable</code>
 * con la información de fallo. El problema reportado puede ser grave
 * o no (un mensaje de error no atrapado a tiempo). De todos modos
 * es algo que puede evitarse.
 * </p>
 * @author Juan F. Codagnone
 * @since Feb 3, 2005
 * @manualitba.panel title="Diálogo de error" screenshot="true"
 * @manualitba.field name="Ignorar" description="Cierra el diálogo, y permite 
 *           continuar, ignorando el problema. Puede terminar cerrando la 
 *           aplicaciónde forma incorrecta, induciendo a perdar algun cambio 
 *           reciente."
 * @manualitba.field name="Relanzar" description="Cierra el diálogo, y relanza 
 *           la excepción. Puede terminar cerrando la aplicación de  forma 
 *           incorrecta, induciendo a perdar algun cambio reciente."
 * @manualitba.field name="Copiar al portapeles" description="Copia el texto en
 *           el portapapeles. Util para generar un reporte. "
 * @manualitba.field name="Terminar" description="Cierra el diálogo, intenta 
 *           guardar el estado del workspace, y termina la aplicación. "
 */
public class JCrashDialog {
    /** mi parte grafico */
    private final JDialog dialog;
    /** se debe relanzar la excepcion */
    private boolean rethrow = false;
    /** texto del padre */
    private String report;
    /** width of the visible dialog */
    private static final int DIALOG_WIDTH = 600;
    /** height of the visible dialog */
    private static final int DIALOG_HEIGHT = 300;
    /** rows to show in the textarea */ 
    private static final int TEXTAREA_ROWS = 120;
    
    /**
     * Crea el JCrashDialog.
     *
     * @param throwable excepcion que causó el crash
     * @param parent padre (if any)
     */
    public JCrashDialog(final Throwable throwable, final JFrame parent) {
        report = generateReport(throwable);
        
        JComponent panel = new Box(BoxLayout.Y_AXIS);
        panel.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        JTextArea area = new JTextArea(report);
        area.setEditable(false);
        area.setRows(TEXTAREA_ROWS);
        area.setAutoscrolls(true);
        panel.add(new JScrollPane(area));
        panel.add(getBotonera());
        
        dialog = new JDialog(parent, "Opps",  true);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);
        //UiUtil.centerAndShow(dialog);
        
        if(rethrow) {
            throw new RuntimeException(throwable);
        }
    }

    /**
     * @param exception excepcion base para el reporte
     * @return el texto del reporte visual del usuario dada una exception
     */
    private static String generateReport(final Throwable exception) {
        StringBuffer sb = new StringBuffer();
        sb.append("Ocurrió algo inesperado!! Seguro es un bug.\n\n");
        sb.append("Contacte a su programador favorito ;)\n\n");
        sb.append("Al reportalo recuerde contar contar como reproducirlo.\n\n");
        //sb.append( "Versión: " + AppFrame.VERSION + "\n\n");
        sb.append("Fecha de suceso: " 
                   + GregorianCalendar.getInstance().getTime()
                   + "\n\n");
        Throwable throwable = exception;
        
        while(throwable.getCause() != null) {
            throwable = throwable.getCause();
        }
        sb.append("Problema raiz: " + throwable + "\n\n");
        
        throwable = exception;
        while(throwable != null) {
            sb.append("Problema: " + throwable);
        
            sb.append("\n\nStackTrace:\n");
            StackTraceElement []err = throwable.getStackTrace();
        
            for(int i = 0; i < err.length; i++) {
                sb.append("  " + err[i].toString());
                sb.append("\n");
            }
            
            throwable = throwable.getCause();
            if(throwable != null) {
                sb.append("causada por: \n");
            }
        }
        
        sb.append("\nPropiedades:\n");
        Properties properties = System.getProperties();
        final int cutLenght = 50;
        final String dotdotdot = "...";
        for(Enumeration e = properties.keys(); e.hasMoreElements();) {
            String key = (String)e.nextElement();
            String val = (String)properties.get(key);
            val = (val == null) ? "" : val;
            if(val.length() > cutLenght) {
                val = val.substring(0, cutLenght - dotdotdot.length())  
                                                                   + dotdotdot;
            }
            
            sb.append("  " + key + "=" + val + "\n");
        }
        
        return sb.toString();
    }

    /**
     * @return el componente visual de la botonera
     */
    private JComponent getBotonera() {
        Box botonera = Box.createHorizontalBox();
        JButton btn = new JButton("Ignorar");
        btn.setToolTipText("Cierra el diálogo, y permite continuar, "
                + "ignorando el problema. Puede terminar cerrando la "
                + "aplicación de forma incorrecta, induciendo a perdar algun "
                + "cambio reciente.");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                JCrashDialog.this.dialog.dispose();
            } 
        });
        botonera.add(btn);
        
        btn = new JButton("Relanzar");
        btn.setToolTipText("Cierra el diálogo, y relanza la excepción. "
                + " Puede terminar cerrando la aplicación de  forma incorrecta,"
                + " induciendo a perdar algun cambio reciente.");
        
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                dialog.dispose();
                rethrow = true;
            } 
        });
        botonera.add(btn);
        
        btn = new JButton("Copiar al portapeles");
        btn.setToolTipText("Copia el texto en el portapapeles. Util para "
                + "generar un reporte. ");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                Clipboard clipboard = Toolkit.getDefaultToolkit()
                        .getSystemClipboard();
                try {
                    clipboard.setContents(new StringSelection(report), null);
                } catch(Throwable e1) {
                    JOptionPane.showMessageDialog(dialog, "Su plataforma no"
                            + "soporta el portapeles: " + e1);
                }
            } 
        });
        botonera.add(btn);
        
        btn = new JButton("Terminar");
        btn.setToolTipText("Cierra el diálogo, y sale de la aplicación");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                // TODO usar una accion
                System.exit(2);
            } 
        });
        botonera.add(btn);
        
        btn = new JButton("Guardar, y salir");
        btn.setToolTipText("Cierra el diálogo, intenta guardar el estado del " 
                + "workspace, y termina la aplicación. ");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                // TODO implementar
                throw new UnsupportedOperationException();
            } 
        });
        btn.setEnabled(false);
        botonera.add(btn);
        
        return botonera;
    }
}