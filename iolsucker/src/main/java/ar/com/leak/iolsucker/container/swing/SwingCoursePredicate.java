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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.apache.commons.collections.Predicate;

import ar.com.leak.common.swing.VerticalLayoutManager;
import ar.com.leak.iolsucker.model.Course;
import ar.com.leak.iolsucker.model.IolDAO;


/**
 * Selector de cursos. Se da la posibilidad de configurar el predicado
 * de forma gráfica la primera vez que se invoca el método evaluate
 * 
 * @author Juan F. Codagnone
 * @since Aug 24, 2005
 */
public class SwingCoursePredicate implements Predicate {
    /** mapa que asocia el codigo de la materia con un boolean que dice si
     *  se usa la materia 
     */
    private final Map<String, Boolean> coursesCodes = 
        new HashMap<String, Boolean>();
    /** el panel */
    private Box mainPanel = Box.createHorizontalBox();
    /** */
    private boolean bFirstTime = true; 
    
    /**
     * Creates the SwingCoursePredicate.
     *
     * @param dao dao de donde sacar la lista de cursos disponibles
     * @param coursesComparator comparator para ordenar la lista de cursos
     */
    public SwingCoursePredicate(final IolDAO dao, 
            final Comparator<Course> coursesComparator) {
        
        final List<Course> courses = 
            new ArrayList<Course>(dao.getUserCourses());
        Collections.sort(courses, coursesComparator);
        final Collection<JCheckBox> checkboxes = new ArrayList<JCheckBox>(
                courses.size());
        final JPanel panel = new JPanel(new VerticalLayoutManager());
        panel.setBorder(new  TitledBorder(new EtchedBorder(), "Cursos"));
        final String courseKey = "course";
        
        for(final Course course : courses) {
            final JCheckBox check = new JCheckBox(new AbstractAction() {
                {   putValue(Action.NAME, course.getName() + " (" 
                            + course.getCode() + ")");
                    putValue(courseKey, course);
                }
                
                public void actionPerformed(final ActionEvent e) {
                    final String courseCode = course.getCode();
                    coursesCodes.remove(courseCode);
                    coursesCodes.put(courseCode, ((JCheckBox)e.getSource()).
                            isSelected());
                }
            });
            check.setSelected(true);
            coursesCodes.put(course.getCode(), true);
            panel.add(check);
            checkboxes.add(check);
        }
        final JPanel box = new JPanel(new VerticalLayoutManager());
        box.setBorder(new  TitledBorder(new EtchedBorder(), "Marcar..."));
        final JButton all = new JButton("Todos");
        all.setToolTipText("Marca todos los cursos");
        all.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                for(final JCheckBox chk : checkboxes) {
                    if(!chk.isSelected()) {
                        chk.doClick();
                    }
                }
            }
        });
        final JButton none = new JButton("Ninguno");
        all.setToolTipText("Desmarca todos los cursos");
        none.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                for(final JCheckBox chk : checkboxes) {
                    if(chk.isSelected()) {
                        chk.doClick();
                    }
                }
            }
        });
        final JButton onlyCourses = new JButton("Sólo Cursos");
        all.setToolTipText("marcá sólo las materias (nivel 4)");
        onlyCourses.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                for(final JCheckBox chk : checkboxes) {
                    final Course course  = ((Course)chk.getAction().
                            getValue(courseKey));
                    if(course.getLevel() == Course.LEVEL_GRADO) {
                        if(!chk.isSelected()) {
                            chk.doClick();    
                        }
                    } else {
                        if(chk.isSelected()) {
                            chk.doClick();    
                        }
                    }
                }
            }
        });
        final JButton onlyAdministrativos = new JButton("Sólo Administrativos");
        all.setToolTipText("marcá sólo las cosas administrativas (no cursos)");
        onlyAdministrativos.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                for(final JCheckBox chk : checkboxes) {
                    final Course course  = ((Course)chk.getAction().
                            getValue(courseKey));
                    if(course.getLevel() == Course.LEVEL_GRADO) {
                        if(chk.isSelected()) {
                            chk.doClick();    
                        }
                    } else {
                        if(!chk.isSelected()) {
                            chk.doClick();    
                        }
                    }
                }
            }
        });
        
        box.add(all);
        box.add(none);
        box.add(onlyCourses);
        box.add(onlyAdministrativos);
        mainPanel.add(panel);
        mainPanel.add(box);
    }

    /** @return la componente swing de esta clase */
    public final Component asComponent() {
        return mainPanel;
    }
    
    /** @see Predicate#evaluate(java.lang.Object) */
    public final boolean evaluate(final Object o) {
       if(bFirstTime) {
           bFirstTime = false;
           JOptionPane.showMessageDialog(null, mainPanel, 
                   "Seleccione las materias a sincronizar", 
                   JOptionPane.CLOSED_OPTION);
       }
       final Course course = (Course)o;
        
       return coursesCodes.get(course.getCode());
    }

}
