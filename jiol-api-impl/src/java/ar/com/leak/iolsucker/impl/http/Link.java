/*
 * Copyright 2005 Juan F. Codagnone <juam at users dot sourceforge dot net>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ar.com.leak.iolsucker.impl.http;

/**
 * Representa un link en una pagina HTML
 * 
 * @author Juan F. Codagnone
 * @since Feb 25, 2005
 */
public class Link {
    /** anchor del link */
    private final String anchor;
    /** uri del link */
    private final String uri;

    /**
     * Crea el Link.
     *
     * @param anchor anchor de link (lo que se ve)
     * @param uri direccion del link
     */
    public Link(final String anchor, final String uri) {

        if(anchor == null || uri == null) {
            throw new AssertionError();
        } else {
            this.anchor = anchor;
            this.uri = uri;
        }
    }

    /** @see java.lang.Object#toString() */
    public final String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("uri=");
        sb.append(uri);
        sb.append("|");
        sb.append("anchor=");
        sb.append(anchor);

        return sb.toString();
    }

    /**
     * @return the link anchor.
     */
    public final String getAnchor() {
        return anchor;
    }

    /**
     * @return the link uri.
     */
    public final String getUri() {
        return uri;
    }
    
    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public final boolean equals(final Object obj) {
        boolean ret = false;
        
        if(obj == this) {
            ret = true;
        } else if(obj instanceof Link) {
            final Link link = (Link)obj;
            ret = link.anchor.equals(anchor) 
                  && link.uri.equals(uri);
        }
        
        return ret;
    }
    
    /** @see java.lang.Object#hashCode() */
    @Override
    public final int hashCode() {
        final int magic1 = 17;
        final int magic2 = 37;
        
        return magic1 + magic2 * uri.hashCode() + magic2 * anchor.hashCode(); 
    }
    
}
