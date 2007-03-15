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

import junit.framework.TestCase;


/**
 * Unit test for {@link ar.com.leak.iolsucker.impl.http.Link}
 *
 * @author Juan F. Codagnone
 * @since Feb 10, 2006
 */
public class LinkTest extends TestCase {

    /** tests {@link Link#equals(Object)} */
    public final void testEquals() {
        
        testEquals(new Link("a", "b"), new Link("a", "b"), 
                new Link("b", "a"));
        
    }
    
    /**
     * @param a an object
     * @param b an object that is equals to a
     * @param c a diferent object
     */
    public final void testEquals(final Object a, final Object b, 
            final Object c) {
        assertFalse(a.equals(null));
        assertFalse(b.equals(null));
        assertFalse(c.equals(null));

        assertNotSame(a, b);

        assertEquals(a, a);
        assertEquals(b, b);
        assertEquals(c, c);

        assertEquals(a, b);
        assertEquals(b, a);
        assertFalse(a.equals(c));

        assertEquals(a.hashCode(), b.hashCode());
    }
}
