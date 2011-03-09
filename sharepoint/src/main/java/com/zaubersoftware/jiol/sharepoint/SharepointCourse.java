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
package com.zaubersoftware.jiol.sharepoint;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.ws.Holder;

import org.apache.commons.lang.Validate;

import ar.com.leak.iolsucker.model.Course;
import ar.com.leak.iolsucker.model.Material;
import ar.com.zauber.leviathan.api.URIFetcher;

import com.microsoft.schemas.sharepoint.soap.ArrayOfSList;
import com.microsoft.schemas.sharepoint.soap.ArrayOfString;
import com.microsoft.schemas.sharepoint.soap.SList;
import com.microsoft.schemas.sharepoint.soap.SiteDataSoap;
import com.zaubersoftware.jiol.sharepoint.items.EventListItemParser;
import com.zaubersoftware.jiol.sharepoint.items.ListItem;
import com.zaubersoftware.jiol.sharepoint.items.ListItemParser;

/**
 * TODO Descripcion de la clase. Los comenterios van en castellano.
 *
 *
 * @author Juan F. Codagnone
 * @since Mar 8, 2011
 */
public class SharepointCourse implements Course {
    private final String name;
    private final String code;
    private final SharepointServiceFactory factory;
    private final ListItemParser listItemParser = new EventListItemParser();
    private static final String MATERIAL = "Material Didáctico";
    private final Charset utf8 = Charset.forName("utf-8");
    /** */
    public SharepointCourse(final String name, final String code, 
            final SharepointServiceFactory factory) {
        Validate.notEmpty(name);
        Validate.notEmpty(code);
        Validate.notNull(factory);
        
        this.name = name;
        this.code = code;
        this.factory = factory;
    }
    
    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final String getCode() {
        return code;
    }

    @Override
    public final int getLevel() {
        return 4;
    }

    @Override
    public final Collection<Material> getFiles() {
        final SiteDataSoap siteDataService = factory.getSiteDataService();
        final Holder<java.lang.Long> n = new Holder<Long>();
        final Holder<ArrayOfSList> lists = new Holder<ArrayOfSList>();
        siteDataService.getListCollection(n, lists);
        
        final Collection<Material> material = new ArrayList<Material>();
        final URIFetcher fetcher = factory.getUriFetcher();
        for(final SList list : lists.value.getSList()) {
            if(MATERIAL.equals(list.getTitle())) {
                final String xml = siteDataService.getListItems(list.getInternalName(), null, 
                                                                null, 1000);
                final List<ListItem> items = listItemParser.parseListItems(
                        new ByteArrayInputStream(xml.getBytes(utf8)));
                
                for(final ListItem item : items) {
                    material.add(new SharepointMaterial(item, fetcher));
                }
            }
        }        
        return material;
    }
}
