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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.*;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;



/**
 * Parsea la pagina de material didáctico y reconoce y extrae las carpetas.
 *
 * @author Juan F. Codagnone
 * @since Feb 10, 2006
 */
public final class LinkFolderParser {
    /** class logger */
    private static final Log LOGGER = LogFactory.getLog(LinkFolderParser.class);
    
    /** Creates the LinkFolderParser. */
    private LinkFolderParser() {
        // utility
    }
    
    /** @see #getLinks(String) */
    public static Collection<Link> getLinks(final BufferedReader br)
            throws IOException {
        return getLinks(LinkParser.getString(br));
    }
    
    
    /**
     * @param text texto a parsear (página de material didáctico de IOL)
     * @throws IOException on error
     * @return Lista de links
     */
    public static Collection<Link> getLinks(final String text)
            throws IOException { 
        final Collection<Link> ret = new ArrayList<Link>();
        
        final Parser parser = new Parser();
        final NodeFilter filter =
            new AndFilter(
                new AndFilter(
                    new NodeClassFilter(TableRow.class),
                    new OrFilter(
                            new HasAttributeFilter("bgcolor", "#F5FFFF"),
                            new HasAttributeFilter("bgcolor", "#E0FFFF")
                            )),
                new NotFilter(new HasAttributeFilter("class")));
        try {
            parser.setEncoding("ISO-8859-1");
            parser.setInputHTML(text);
            final NodeList list = parser.extractAllNodesThatMatch(filter);
           
            for(int i = 0; i < list.size(); i++) {
                final Node node = list.elementAt(i);
                final Collection<Link> links = LinkParser.getLinks(node
                        .toHtml());
                if(links.size() == 1) {
                    final String s = node.toPlainTextString().trim();
                    final int c = s.indexOf('\n');
                    if(c != -1) {
                        final String folderName = s.substring(0, c).trim();
                        String l = links.toArray(new Link[]{})[0].getUri();
                        ret.add(new Link(folderName, l));
                    }
                }
            }
        } catch(ParserException e) {
            LOGGER.error("loading html", e);
            throw new IOException(e.getMessage());
        }
        
        return ret;
    }

}
