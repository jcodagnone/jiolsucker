package com.zaubersoftware.jiol.sharepoint;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.List;

import javax.xml.ws.Holder;

import ar.com.leak.iolsucker.model.impl.common.login.ParameterLoginInfo;

import com.microsoft.schemas.sharepoint.soap.ArrayOfSList;
import com.microsoft.schemas.sharepoint.soap.ArrayOfString;
import com.microsoft.schemas.sharepoint.soap.SList;
import com.microsoft.schemas.sharepoint.soap.SiteDataSoap;
import com.zaubersoftware.jiol.sharepoint.items.EventListItemParser;
import com.zaubersoftware.jiol.sharepoint.items.ListItem;
import com.zaubersoftware.jiol.sharepoint.items.ListItemParser;

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

/**
 * TODO Descripcion de la clase. Los comenterios van en castellano.
 * 
 * 
 * @author Juan F. Codagnone
 * @since Mar 1, 2011
 */
public final class SharepointFacadeDriver {

    /** utitlity class */
    private SharepointFacadeDriver() {
        // void
    }

    private static final String KEY_USERNAME = "jiol.username";
    private static final String KEY_PASSWORD = "jiol.password";

    /** main */
    public static void main(final String[] args) throws Exception {
        final String username = System.getProperty(KEY_USERNAME);
        final String password = System.getProperty(KEY_PASSWORD);

        if (null == username) {
            throw new IllegalStateException(
                    "Falta definir la propiedad del sistema " + KEY_USERNAME);
        }
        if (null == password) {
            throw new IllegalStateException(
                    "Falta definir la propiedad del sistema " + KEY_PASSWORD);
        }

        final SharepointServiceFactory factory = new JAXWSharepointServiceFactory(
                new FixedURISharepointStrategy(
                        URI.create("http://iol2.itba.edu.ar:27521")),
                new ParameterLoginInfo(username, password), "72.27");
        final ListItemParser listItemParser = new EventListItemParser(); 
        
        final SiteDataSoap siteDataService = factory.getSiteDataService();
        final Holder<java.lang.Long> n = new Holder<Long>();
        final Holder<ArrayOfSList> lists = new Holder<ArrayOfSList>();
        siteDataService.getListCollection(n, lists);
        for(final SList list : lists.value.getSList()) {
            if("Material Didáctico".equals(list.getTitle())) {
            System.out.println(list.getTitle());
                final String xml = siteDataService.getListItems(list.getInternalName(), null, 
                                                                null, 1000);
                final List<ListItem> items = listItemParser.parseListItems(
                        new ByteArrayInputStream(xml.getBytes("utf-8")));
                
                for(ListItem item : items) {
                    System.out.println(item);
                    Holder<ArrayOfString> arrays = new Holder<ArrayOfString>();
                }
                System.out.println("-----------------");
            }
        }
    }

}
