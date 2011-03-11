/*
 *  Copyright 2005 Eduardo Pereda IV
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
package ar.com.leak.common.swing;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 *
 * This class offers some static method convenient for setting some
 * common properties for a {@link java.awt.GridBagConstraints}.
 *
 * @author Eduardo Pereda IV
 * @since 05/09/2005
 */
public final class GridBagConstraintsHelper {

    /**
     * We don't want instances of this class.
     */
    private GridBagConstraintsHelper() {
    }

    /**
     * This method returns a {@link GridBagConstraints} instances
     * that has some of its properties set so it can be used as
     * the constraints asociated with one of the columns of a table
     * made using a {@link java.awt.GridBagLayout}.
     * @param colNumber The number of the column, starting at 0. Will be the
     * value for the property <code>gridx</code>.
     * @param anchor as specified by {@link GridBagConstraints}
     * @param weightx as specified by {@link GridBagConstraints}
     * @param fill as specified by {@link GridBagConstraints}
     * @param inset as specified by {@link GridBagConstraints}
     * @return the GridBagConstraints with the properties set.
     */
    public static GridBagConstraints getColumnConstraint(final int colNumber,
            final int anchor, final double weightx, final int fill,
            final int inset) {
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = colNumber;
        constraints.anchor = anchor;
        constraints.weightx = weightx;
        constraints.weighty = 1;
        constraints.fill = fill;
        constraints.insets = new Insets(inset, inset, inset, inset);
        return constraints;
    }
}
