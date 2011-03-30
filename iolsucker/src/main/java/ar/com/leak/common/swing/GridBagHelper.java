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
package ar.com.leak.common.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * This class is a helper that will help adding JComponents to a JComponent
 * using the {@link java.awt.GridBagLayout}.
 *
 * @author Eduardo Pereda IV
 * @since 05/09/2005
 * @see java.awt.GridBagLayout
 * @see java.awt.GridBagConstraints
 */
public class GridBagHelper implements JComponentable {

    /** This is the panel that will have components as a GridBag */
    private final JComponent panel;
    /**
     * This are the constraints that will be applied to each element
     * that is added to the panel.
     */
    private GridBagConstraints constraints;
    /** Instance of the GridBagLayout that is used as default */
    private final GridBagLayout layout = new GridBagLayout();;

    /**
     *
     * Creates the GridBagHelper. This default constructor will
     * use a JPanel as a JComponent.
     *
     */
    public GridBagHelper() {
        this.constraints = new GridBagConstraints();
        this.panel = new JPanel(this.layout);
    }

    /**
     *
     * Creates the GridBagHelper.
     *
     * @param component The component to which this object will be
     * applied.
     * @param constraints The constraints that will be used as default
     * by the {@link #add(JComponent)} method.
     */
    public GridBagHelper(final JComponent component,
            final GridBagConstraints constraints) {
        this.panel = component;
        this.panel.setLayout(this.layout);
        this.constraints = constraints;
    }
    /**
     *
     * Creates the GridBagHelper.
     *
     * @param component The component to which this object will be
     * applied.
     */
    public GridBagHelper(final JComponent component) {
        this(component, new GridBagConstraints());
    }

    /**
     * @inheritDoc
     * @return the JComponent that this object applies to.
     */
    public final JComponent asJComponent() {
        return panel;
    }

    /**
     * Adds a new component to the JComponent using the default
     * constraints as specified by the constructor or the
     * {@link #setConstraints(GridBagConstraints)} method.
     * If a specific constraint is never specified, the default
     * constraints as specified by
     * {@link GridBagConstraints#GridBagConstraints()} will be used.
     * @param component The component to be added.
     */
    public final void add(final JComponent component) {
        add(component, this.constraints);
    }
    /**
     * Similar to {@link #add(JComponent)}, but using the suplied
     * constraints.
     * @param component The component to be added.
     * @param supliedConstraints The constraints to be used.
     */
    public final void add(final JComponent component,
            final GridBagConstraints supliedConstraints) {
        layout.setConstraints(component, supliedConstraints);
        panel.add(component);
    }

    /**
     * @return the default constraints for this object
     */
    public final GridBagConstraints getConstraints() {
        return constraints;
    }
    /**
     * @param constraints The constraints that will be used as
     * default in the {@link #add(JComponent)} method.
     */
    public final void setConstraints(final GridBagConstraints constraints) {
        if(constraints == null) {
            throw new IllegalArgumentException("constraints");
        }
        this.constraints = constraints;
    }
}
