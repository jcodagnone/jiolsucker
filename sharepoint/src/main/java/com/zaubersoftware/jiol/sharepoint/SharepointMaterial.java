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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.Validate;

import ar.com.leak.iolsucker.model.Material;
import ar.com.zauber.leviathan.api.URIFetcher;
import ar.com.zauber.leviathan.api.URIFetcherHttpResponse;
import ar.com.zauber.leviathan.api.URIFetcherResponse;

import com.zaubersoftware.jiol.sharepoint.items.ListItem;

/**
 * Sharepoint implementation 
 * 
 * @author Juan F. Codagnone
 * @since Mar 8, 2011
 */
public class SharepointMaterial implements Material {
    private final ListItem item;
    private final URIFetcher fetcher;

    /** Creates the SharepointMaterial. */
    public SharepointMaterial(final ListItem item, final URIFetcher fetcher) {
        Validate.notNull(item);
        Validate.notNull(fetcher);
        
        this.item = item;
        this.fetcher = fetcher;
    }
    
    @Override
    public final boolean isFolder() {
        return false;
    }

    @Override
    public final String getName() {
        return item.get("ows_ServerUrl");
    }

    @Override
    public final InputStream getInputStream() throws IOException {
        final URI uri = item.get("ows_EncodedAbsUrl", URI.class);
        final URIFetcherResponse response = fetcher.get(uri);
        if(response.isSucceeded()) {
            final URIFetcherHttpResponse r = response.getHttpResponse();
            if(r.getStatusCode() != 200) {
                throw new IOException("Incorrect status code " + r.getStatusCode()
                        + " for " + uri.toString());
            }
            return r.getRawContent();
        } else {
            throw new IOException(response.getError());
        }
    }

    @Override
    public final long getEstimatedSize() {
        return item.contains("ows_FileSizeDisplay") ? 0 :  item.get("ows_FileSizeDisplay", Long.class);
    }

    public final Date getLastModified() {
        return item.contains("ows_Modified") ? new Date(0) : item.get("ows_Modified", Date.class);
    }
}