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
package ar.com.leak.iolsucker.impl.http.extras;

import java.io.*;
import java.net.*;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.com.leak.iolsucker.impl.http.HTTPClient;
import ar.com.leak.iolsucker.impl.http.Link;
import ar.com.leak.iolsucker.impl.http.LinkParser;
import ar.com.leak.iolsucker.impl.http.HTTPClient.MethodEnum;
import ar.com.leak.iolsucker.model.Material;


/**
 * Implementación de MaterialFile que emula el comportamiento del viejo 
 * iolsucker-2 a la hora de obtener el contenido del archivo. Esta forma 
 * garantiza que si el archivo estaba marcado como no leido, va a seguir estando
 * no leido, pero la forma de  obtener el link es mas debil, y se puede romper
 * mas facil. De hecho, como las urls de iol suelen no estar codificando se
 * debe encodear a mano, y esto es bastante sucio.
 *
 * @author Juan F. Codagnone
 * @since Aug 23, 2005
 */
public class OldFashionHttpMaterialFile implements Material {
    /** logger... */
    private static Log logger = 
        LogFactory.getLog(OldFashionHttpMaterialFile.class);
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
    /** url de donde descargar el archivo */
    private URL url = null;
    /** fecha de última modificación */
    private Date lastModified;
    
    /**
     * Crea el HTTPMaterialFile.
     *
     * @param client http client to use
     * @param parent folder address that holds this file
     * @param fid file id 
     */
    public OldFashionHttpMaterialFile(final HTTPClient client, 
            final String parent, final int fid) {
        Validate.notNull(client, "client");
        Validate.notNull(parent, "parent no puede ser nulo");
        
        this.fid = fid;
        this.client = client;
        this.parent = parent;
    }

    /** @see ar.com.leak.iolsucker.model.Material#isFolder() */
    public final boolean isFolder() {
        return false;
    }
    
    /**
     * @param huc conexión de donde sacar los dats
     * @throws IOException on error
     */
    final void loadValues(final URLConnection huc) throws IOException {
        huc.connect();
        final InputStream in = huc.getInputStream();
         final Collection<Link> links = LinkParser.getLinks(
                new BufferedReader(new InputStreamReader(in, 
                        "iso-8859-1")));
        in.close();
        
        for(final Link link : links) {
            if(link.getAnchor().equals("Bajar")) {
                String uri = link.getUri();
                if(uri.startsWith("/")) {
                    final URL u = client.getNamingMapper().getURLBase();
                    uri  = u.getProtocol() + "://" + u.getHost() + uri;
                    name = parent + File.separatorChar 
                              + uri.substring(uri.lastIndexOf('/') + 1);
                    url = getURL(uri);
                    final URLConnection hh = client.getConnection(
                            url, MethodEnum.HEAD, null);
                    hh.connect();
                    lastModified = new Date(hh.getLastModified());
                    size = hh.getContentLength();
                }
            }
        }
    }
    
    /** Carga el nombre del archivo, y el tamaño */
    private void loadValues() {
        if(name == null) {
            try {
                loadValues(client.getConnection(client.getNamingMapper().
                        getFileHeader(fid)));
                if(url == null) {
                    throw new RuntimeException("blee"); 
                }
            } catch(IOException e) {
                logger.error("obteniendo nombre de archivo", e);
                throw new RuntimeException(e);
            }
        }
    }
    
    /**
     * @param url url...en general viene mal encodeada (con espacios y acentos)
     * @return la url correspondiente al parametro, debidamente encodeado
     * @throws MalformedURLException  on error
     */
    private static URL getURL(final String url) 
      throws MalformedURLException {
        final StringBuilder sb = new StringBuilder();
        final int asciiLimit = 128;
        for(int i = 0; i < url.length(); i++) {
            final char c = url.charAt(i);
            if(c >= asciiLimit) {
                try {
                    sb.append(URLEncoder.encode(String.valueOf(c), "utf8"));
                } catch(final Exception e) {
                    throw new RuntimeException(e);
                }
            } else if(c == ' ') {
                sb.append("%20");
            } else {
                sb.append(c);
            }
            
        }
        return new URL(sb.toString());
    }

    /** @see ar.com.leak.iolsucker.model.Material#getReader() */
    public final InputStream getInputStream() throws IOException {
        final URLConnection huc = client.getConnection(url, MethodEnum.GET, 
                null);
        huc.connect();
        return  huc.getInputStream();
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
        loadValues();
        
        return lastModified;
    }
}
