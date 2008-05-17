package ar.com.leak.iolsucker.container.swing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.JFrame;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.Filter;

import ar.com.leak.iolsucker.container.Options;

/**
 * JFrame que hace de wrapper para un
 * {@link ar.com.leak.iolsucker.container.swing.ProgressView} y le agrega
 * la opción para determinar si se debe iniciar la descarga de archivos sin
 * preguntar la siguiente vez que se inicie el programa.
 *
 * @author Eduardo Pereda
 * @since Oct 1, 2005
 */
public class JFrameProgressView extends JFrame {
    /** Opciones que pueden ser modificadas por este objeto */
    private final Options options;
    /**
     * Crea el JFrameProgressView.
     *
     * @param title
     *            Título para la ventana
     * @param options
     *            para que este objeto determine si tiene que mostrar o no la
     *            opción de "iniciar la descarga de archivos sin preguntar" y
     *            que pueda modificarla.
     * @param layout Idem a
     * {@link ProgressView#ProgressView(Layout, Filter, Action)}
     * @param filter Idem a
     * {@link ProgressView#ProgressView(Layout, Filter, Action)}
     * @param action Idem a
     * {@link ProgressView#ProgressView(Layout, Filter, Action)}
     * @see ProgressView
     */
    public JFrameProgressView(final String title, final Options options,
            final Layout layout, final Filter filter, final Action action) {
        super(title);
        this.options = options;
        final int panelWidth = 640;
        final int panelHeight = 340;

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        final Box vBox = Box.createVerticalBox();
        vBox.add(new ProgressView(layout, filter, action));
        vBox.add(getStartWithoutPromptingOption());
        vBox.setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.setContentPane(vBox);
    }

    /**
     * @return la parte visual que configura la opción de si durante
     * la siguiente ejecucion se debe empezar directamente bajando jiol
     */
    private JComponent getStartWithoutPromptingOption() {
        final Box h = Box.createHorizontalBox();
        if(options.isSaveData() && options.isSavePassword()) {
            final JCheckBox checkBox = new JCheckBox(
                "La próxima vez iniciar la descarga de archivos sin preguntar");
            checkBox.setSelected(options.isStartWithoutPrompting());
            checkBox.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    options.setStartWithoutPrompting(((JCheckBox)e.getSource())
                            .isSelected());
                }
            });
            h.add(checkBox);
        }
        return h;
    }

}
