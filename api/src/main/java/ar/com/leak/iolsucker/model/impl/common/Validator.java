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
package ar.com.leak.iolsucker.model.impl.common;

/**
 * Validador de cosas 
 * 
 * @author Juan F. Codagnone
 * @since Jul 6, 2005
 */
public interface Validator {

    /**
     * @param object objeto a testeer
     * @return <code>true</code> si algo es valido
     */
    boolean isValid(Object object);

}
