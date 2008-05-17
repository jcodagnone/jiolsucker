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
package ar.com.leak.iolsucker.container.common;

import junit.framework.TestCase;


/**
 * Testeos de unidad para 
 * ar.com.leak.iolsucker.container.swing.impl.PasswordCoder
 * 
 * @see ar.com.leak.iolsucker.container.swing.impl.PasswordCoder
 * @author Juan F. Codagnone
 * @since Apr 12, 2005
 */
public class PasswordCoderTest extends TestCase {
    /** lista de palabras a probar */
    private final String []passwords = {
            "unaPalabra",
            "varias palabras",
            "varias palabras con chirimbolos!! áéññ¢",
    };
    
    /**
     * debe haber un algoritmo default
     */
    public final void testGetPreferedAlgorithm() {
        String s = PasswordCoder.getPreferedAlgorithm();
        assertNotNull(s);
        assertTrue(s.length() != 0);
    }

    /**
     * teste que codificando y decodificando un string, se llegue a lo mismo,
     * y que la codificación no sea nula, o igual 
     */
    public final void testCodeAndDecode() {
        final String [] algos = PasswordCoder.getAlgorithms();
        for(int i = 0; i < algos.length; i++) {
            final String algo = algos[i];
            System.out.println("trying algorithm: " + algo);
            for(int j = 0; j < passwords.length; j++) {
                String s = passwords[j];
                String pass = PasswordCoder.code(algo, s);
                assertNotNull(pass);
                assertFalse(s.equals(pass));
                String deco = PasswordCoder.decode(algo, pass);
                System.out.println(s + " vs " + deco);
                assertEquals(s, deco);
            }
        }
    }
}
