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
package ar.com.leak.iolsucker.impl.http.util;

import junit.framework.TestCase;

/**
 * TODO Agregar comentarios pertinentes
 * 
 * @author Juan F. Codagnone
 * @since Apr 22, 2005
 */
public class URIHelperTest extends TestCase {

    /**
     * @see URIHelper#isContainedIn(URIHelper)
     */
    public final void testIsContainedIn() {
        final URIHelper [] uris = {
                new URIHelper("mynav.asp?cmd=help"),
                new URIHelper("mynav.asp?cmd=help&param1=something"),
                new URIHelper("mynav.asp?param1=something"),
                new URIHelper("other.asp?cmd=help"), new URIHelper("a"), };
        final boolean [][]expectedResults = {
                {true, true, false, false, false },
                {false, true, false, false, false },
                {false, true, true, false, false },
                {false, false, false, true, false },
                {false, false, false, false, true }, };
        assertEquals(uris.length, expectedResults.length);

        for(int i = 0; i < expectedResults.length; i++) {
            boolean [] row = expectedResults[i];
            assertEquals(expectedResults.length, row.length);

            for(int j = 0; j < row.length; j++) {
                boolean ret = uris[i].isContainedIn(uris[j]);
                // System.out.println("'" + uris[i] + "' is contained in '" +
                // uris[j] + "' ? "+ ret);
                assertEquals(row[j], ret);
            }
        }
    }
}
