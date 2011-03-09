/*
 *  Copyright 2011 Juan F. Codagnone <juam at users dot sourceforge dot net>
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
package com.zaubersoftware.jiol.sharepoint.items;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import ar.com.zauber.commons.dao.Transformer;


/**
 * TODO Descripcion de la clase. Los comenterios van en castellano.
 * 
 * 
 * @author Juan F. Codagnone
 * @since Mar 8, 2011
 */
public class MapListItemTest {

    /** prueba el transformer de date */
    @SuppressWarnings("deprecation")
    @Test
    public final void testDate() {
        final Transformer t = MapListItem.transformer().get(Date.class);
        final Date date = (Date) t.transform("2011-02-25T19:35:25Z");
        assertEquals(111, date.getYear());
        assertEquals(1, date.getMonth());
        assertEquals(25, date.getDate());
        assertEquals(35, date.getMinutes());
        assertEquals(25, date.getSeconds());
    }
}
