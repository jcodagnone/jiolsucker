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
package ar.com.leak.iolsucker.impl.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.Date;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.com.leak.iolsucker.model.Material;


/**
 * Implementación HTTP de un Material Archivo
 * 
 * @author Juan F. Codagnone
 * @since Feb 26, 2005
 */
public class HTTPMaterialFile implements Material {
    /** logger... */
    private static Log logger = LogFactory.getLog(HTTPMaterialFile.class);
    /** file id de este archivo */
    private final int fid;
    /** cliente http que se usa */
    private final HTTPClient client;
    /** carpeta padre */
    private final String parent;
    /** nombre del archivo (se carga on the run) */
    private String name = null;
    /** tamaño del archivo (se carga on the run) */
    private long size = -1;
    
    /**
     * Crea el HTTPMaterialFile.
     *
     * @param client http client to use
     * @param parent folder address that holds this file
     * @param fid file id 
     */
    public HTTPMaterialFile(final HTTPClient client, final String parent, 
            final int fid) {

        if(parent == null) {
            throw new NullArgumentException("parent no puede ser nulo");
        }
        this.fid = fid;
        this.client = client;
        this.parent = parent;
    }

    /** @see ar.com.leak.iolsucker.model.Material#isFolder() */
    public final boolean isFolder() {
        return false;
    }

    /** 
     * @return una conexión resultado de hacer HEAD a la url del archivo
     * @throws IOException on error 
     */
    private URLConnection headHUC(final String url) throws IOException {
        return client.headConnection(url);
    }
    
    /**
     *  @return una conexión resultado de hacer GET a la url del archivo
     *  @throws IOException on error 
     */
    private URLConnection getHUC() throws IOException {
        return client.getConnection(client.getNamingMapper().
                getDownloadFile(fid));
    }
    
    /** @param huc carga el nombre del archivo */
    private void loadValues(final URLConnection huc) {
        size = huc.getContentLength();
        String tmp = huc.getHeaderField("Content-Disposition");
        Validate.notNull(tmp, "se esperaba el header Content-Disposition "
                + "para extraer el nombre de archivo, pero no vino.");
        tmp = tmp.substring(tmp.indexOf(';'));
        tmp = tmp.substring(tmp.lastIndexOf('=') + 1).trim();
        name = parent + File.separatorChar + tmp;
    }
    
    /** TODO que hacia esto? */
    private void loadValues() {
        if(name == null) {
            try {
                final String url = client.getNamingMapper().getDownloadFile(fid);
                final URLConnection huc = headHUC(url);
                if(huc instanceof HttpURLConnection 
                     && ((HttpURLConnection) huc).getResponseCode() != 200) {
                    final HttpURLConnection c = (HttpURLConnection) huc; 
                    throw new RuntimeException(
                            "obteniendo nombre de archivo usando " 
                            + url
                            + ". El server retornó " 
                            + c.getResponseCode()
                            + " " + c.getResponseMessage());
                }
                loadValues(huc);
            } catch(IOException e) {
                logger.error("obteniendo nombre de archivo" + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }
    
    /** @see ar.com.leak.iolsucker.model.Material#getReader() */
    public final InputStream getInputStream() throws IOException {
        URLConnection huc =  getHUC();
        InputStream in = huc.getInputStream();
        loadValues(huc);
        return in;
    }
    
    /** @see ar.com.leak.iolsucker.model.Material#getName() */
    public final String getName() {
        loadValues();
        
        return name;
    }

    /** @see ar.com.leak.iolsucker.model.Material#getEstimatedSize() */
    public final long getEstimatedSize() {
        loadValues();
        
        return size;
    }

    /** @see java.lang.Object#toString() */
    public final String toString() {
        return Integer.toString(fid);
    }

    /** @see ar.com.leak.iolsucker.model.Material#getLastModified() */
    public final Date getLastModified() {
        // TODO ver si se puede implementar
        return new Date(0);
    }
}
