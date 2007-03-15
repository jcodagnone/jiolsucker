/*
 *  Copyright 2006 Juan F. Codagnone <juam at users dot sourceforge dot net>
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
package ar.com.leak.iolsucker.impl.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import junit.framework.TestCase;


/**
 *  Units tests for {@link LinkFolderParser#getLinks(String)}
 *
 * @author Juan F. Codagnone
 * @since Feb 10, 2006
 */
public class LinkFolderParserTest extends TestCase {

    /**  
     * tests {@link LinkFolderParser#getLinks(String)}
     * @throws IOException on error
     */
    public final void testFolderParser() throws IOException {
        final ClassLoader loader = getClass().getClassLoader();
        Link[] expected;
        Collection<Link> ret;
        
        expected = new Link[] {
               new Link("Acreditacion CONEAU", "newmaterialdid.asp?fid=28084"),
               new Link("Boletin General 2005", "newmaterialdid.asp?fid=25138"),
               new Link("Disposiciones ITBA", "newmaterialdid.asp?fid=31617"),

        };
        
        ret = LinkFolderParser.getLinks(new BufferedReader(
                new InputStreamReader(loader
                        .getResourceAsStream("material/user.html"))));

        assertTrue(Arrays.equals(expected, ret.toArray()));
        
        expected = new Link[] {
        };
        ret = LinkFolderParser.getLinks(new BufferedReader(
                new InputStreamReader(loader
                        .getResourceAsStream("material/user1.html"))));
        assertTrue(Arrays.equals(expected, ret.toArray()));
    }
}
