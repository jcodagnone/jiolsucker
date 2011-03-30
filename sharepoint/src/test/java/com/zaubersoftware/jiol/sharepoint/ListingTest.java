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
package com.zaubersoftware.jiol.sharepoint;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.zaubersoftware.jiol.sharepoint.items.EventListItemParser;
import com.zaubersoftware.jiol.sharepoint.items.ListItem;

/**
 * TODO Descripcion de la clase. Los comenterios van en castellano.
 *
 *
 * @author Juan F. Codagnone
 * @since Mar 8, 2011
 */
public class ListingTest {
    
    /** prueba el parseo */
    @Test
    public final void testParse() {
        final InputStream is = getClass().getClassLoader().getResourceAsStream(
                "listitems.xml");
        final List<ListItem> items = new EventListItemParser().parseListItems(is);
        assertEquals(2, items.size());
        final ListItem item1 = items.get(0);
        final ListItem item2 = items.get(1);
        
        assertEquals("{EAACB35C-85A5-4467-91DC-E2F699CD5ACC}", item1.get("ows_GUID"));
        assertEquals(Long.valueOf(45569L), item1.get("ows_FileSizeDisplay", Long.class));
        assertNotNull(item1.get("ows_EncodedAbsUrl", URI.class));
        assertEquals("{F41A6B72-061E-484E-8B49-22F6FCD75941}", item2.get("ows_GUID"));
        assertNotNull(item1.get("ows_Modified", Date.class));
    }    
}
