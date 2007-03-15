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

import java.io.*;
import java.net.URL;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * Levanta todos los links que existen en un html
 * 
 * @author Juan F. Codagnone
 * @since Feb 25, 2005
 */
public final class LinkParser {
    /** class logger */
    private static final Log LOGGER = LogFactory.getLog(LinkParser.class);

    /**
     * Crea el LinkParser.
     */
    private LinkParser() {
        // utility class. checkstyle suggestion
    }

    
    /**
     * @param br html page
     * @return a collection of Links contained in the input page
     * @throws IOException on error
     */
    public static Collection<Link> getLinks(final BufferedReader br) 
    throws IOException {
            return getLinks(getString(br));
    }

    /** @see #getLinks(BufferedReader) */
    public static Collection<Link> getLinks(final String s) 
    throws IOException {
        final List<Link> results = new LinkedList<Link>();
        final NodeClassFilter filter = new NodeClassFilter(LinkTag.class);
        final Parser parser = new Parser();
        try {
            parser.setEncoding("ISO-8859-1");
            parser.setInputHTML(s);
            final NodeList list = parser.extractAllNodesThatMatch(filter);
            for(int i = 0; i < list.size(); i++) {
                final LinkTag link = (LinkTag)list.elementAt(i);
                final String anchor = link.getLinkText().trim();
                String uri = link.extractLink();
                if(uri.equals("#")) {
                    uri = link.getAttribute("onClick").trim();
                }
                uri = uri.replaceAll("&amp;", "&").trim();
                if(uri.length() != 0) {
                    results.add(new Link(anchor, uri));
                }

            }
        } catch(ParserException e) {
            LOGGER.error("loading html", e);
            throw new IOException(e.getMessage());
        }

        return results;
    }
    /**
     * @param source stream con la pagina html
     * @return el string con todo el contenido de source
     * @throws IOException on error
     */
    static String getString(final BufferedReader source) throws IOException {
        final StringBuffer sb = new StringBuffer();
        final int oneKB = 1024;
        final char [] buffer = new char[1 * oneKB];
        int length;

        while((length = source.read(buffer)) >= 0) {
            sb.append(buffer, 0, length);
        }

        return sb.toString();
    }

    /**
     * quick test 
     * @param args command line parameters
     * @throws Exception on error
     */
    public static void main(final String [] args) throws Exception {
        final URL url = new URL(args[0]);
        final InputStream in = url.openStream();
        final Collection c = LinkParser.getLinks(new BufferedReader(
                new InputStreamReader(in)));
        in.close();
        
        for(Iterator i = c.iterator(); i.hasNext();) {
            System.out.println(i.next());
        }
    }
}
