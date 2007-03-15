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

import javax.swing.JComponent;

/**
 *
 * This interface just defines that the classes that implement it
 * can be represented as a {@link javax.swing.JComponent}.
 *
 * @author Eduardo Pereda IV
 * @since 04/09/2005
 */
public interface JComponentable {
    /**
     * @return a {@link JComponent} representation of this object.
     */
    JComponent asJComponent();
}
