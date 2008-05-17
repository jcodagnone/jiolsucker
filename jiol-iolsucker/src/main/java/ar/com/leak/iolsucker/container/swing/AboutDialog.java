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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.*;

import ar.com.leak.iolsucker.ProjectInfo;

/**
 * 
 * Clasico dialogo que muestra información de un programa
 * 
 * @author Juan F. Codagnone
 * @since Apr 12, 2005
 */
public class AboutDialog extends JDialog {
    /** root panel */
    private final Box panel = Box.createVerticalBox();
    /** información del proyecto */
    private final ProjectInfo info;
    
    /**
     * Creates the AboutDialog.
     *
     * @param owner padre
     * @param info información del proyecto
     */
    public AboutDialog(final Frame owner, final ProjectInfo info) {
        super(owner, "Sobre el programa...", true);
        this.info = info;
        
        panel.add(new JLabel("<html><b>" + info.getProjectName() + " "
                + info.getProjectVersion() + "</b></html>"));
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Sobre...", getAboutPanel());
        tabs.add("Desarrolladores", getDeveloperPanel());
        tabs.add("Agradecimientos", getContributorsPanel());
        tabs.add("Licencia", getLicensePanel());
        panel.add(tabs);
        final int panelWidth = 480;
        final int panelHeight = 260;
        panel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.getContentPane().add(panel);
    }
    
    /** @return un panel... */
    private Component getAboutPanel() {
        return getPanel(info.getProjectDescription());
    }
    
    /** @return un panel con la información de los desarrolladores */
    private Component getDeveloperPanel() {
        StringBuffer sb = new StringBuffer();
        sb.append("Desarrolladores del proyecto: \n");
        sb.append("\n");
        String []devs = info.getDevelopers();
        for(int i = 0; i < devs.length; i++) {
            sb.append("  o ");
            sb.append(devs[i]);
            sb.append("\n");
        }
        return getPanel(sb.toString());
    }
    
    /** @return un panel con la información de los contributors */
    private Component getContributorsPanel() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Gente que ha ayudado de una u otra manera "
                + "con el proyecto: \n");
        sb.append("\n");
        final String []contrib = info.getContributors();
        for(int i = 0; i < contrib.length; i++) {
            sb.append("  o ");
            sb.append(contrib[i]);
            sb.append("\n");
        }
        return getPanel(sb.toString());
    }
    
    /** @return un panel con la información de la licencia */
    private Component getLicensePanel() {
        return getPanel(info.getLicence());
    }
    
    /** 
     * @return crae un lindo panel que muestra la info de s
     * @param s texto a mostrar
     */
    private Component getPanel(final String s) {
        final Box box = Box.createHorizontalBox();
        JTextArea area = new JTextArea(s);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        box.add(scroll);
        return box;
    }
}
