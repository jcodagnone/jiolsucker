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
package com.zaubersoftware.jiol.sharepoint.items;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.UnhandledException;

/**
 * Parsea los {@link ListItem} de un sitio SharePoint. No le presta atencion a
 * la definición de esquema. Basado en atributos.
 * 
 * Utiliza una maquina de estados para recorrer el xml (el xml es dinamico por
 * lo que no se puede parsear con jaxb).
 * 
 * @author Juan F. Codagnone
 * @since Mar 8, 2011
 */
public class EventListItemParser implements ListItemParser {

    @Override
    public final List<ListItem> parseListItems(final InputStream is) {
        try {
            return parseListItemsInternal(is);
        } catch (final XMLStreamException e) {
            throw new UnhandledException(e);
        } catch (final IOException e) {
            throw new UnhandledException(e);
        }
    }

    /** internal method */
    private List<ListItem> parseListItemsInternal(final InputStream is) 
     throws XMLStreamException, IOException {
        try {
            final XMLEventReader reader = XMLInputFactory.newFactory()
                    .createXMLEventReader(is);
            final ParsingCtx ctx = new ParsingCtx();
            XDRStateMachineParser parser = XDRStateMachineParser.XML;

            while (reader.hasNext()) {
                final XMLEvent event = reader.nextEvent();
                StartElement start = null;
                EndElement end = null;

                if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    start = event.asStartElement();
                    parser = parser.parse(start, ctx);
                } else if (event.getEventType() == XMLStreamConstants.END_ELEMENT) {
                    end = event.asEndElement();
                    parser = parser.parse(end, ctx);
                }
//                System.out.println(parser
//                        + "|"
//                        + event.getEventType()
//                        + "|"
//                        + (start == null ? null : start.getName()
//                                .getLocalPart()) + "|"
//                        + (end == null ? null : end.getName().getLocalPart()));
                if (parser == null) {
                    throw new IllegalArgumentException();
                }
            }
            return ctx.items;
        } finally {
            is.close();
        }
    }
}

/**
 * State Machine to parse the List Item XML.
 * 
 * @author Juan F. Codagnone
 * @since Mar 8, 2011
 */
enum XDRStateMachineParser {
    /** <xml> */
    XML {
        @Override
        public XDRStateMachineParser parse(final StartElement event,
                final ParsingCtx ctx) {
            return SCHEMA_OR_DATA;
        }

        @Override
        public XDRStateMachineParser parse(final EndElement event,
                final ParsingCtx ctx) {
            return END;
        }
    },
    /** <s:Schema> */
    SCHEMA_OR_DATA {

        @Override
        public XDRStateMachineParser parse(final StartElement event,
                final ParsingCtx ctx) {
            XDRStateMachineParser ret = null;
            if (QSCHEMA.equals(event.getName())) {
                ret = SCHEMA;
            } else if (QDATA.equals(event.getName())) {
                ret = DATA;
            } else {
                fail(event);
            }
            return ret;
        }

        @Override
        public XDRStateMachineParser parse(final EndElement event,
                final ParsingCtx ctx) {
            return END;
        }
    },
    /** s:ElementType  */
    SCHEMA {

        @Override
        public XDRStateMachineParser parse(final StartElement event,
                final ParsingCtx ctx) {
            return SCHEMA;
        }

        @Override
        public XDRStateMachineParser parse(final EndElement event,
                final ParsingCtx ctx) {
            if (QSCHEMA.equals(event.getName())) {
                return SCHEMA_OR_DATA;
            }
            return SCHEMA;
        }

    },
    /** rs:data */
    DATA {

        @Override
        public XDRStateMachineParser parse(final StartElement event,
                final ParsingCtx ctx) {
            XDRStateMachineParser ret = null;
            if (QROW.equals(event.getName())) {
                ret = DATA;
                final Map<String, String> item = new HashMap<String, String>();
                for (Iterator<Attribute> i = event.getAttributes(); i.hasNext();) {
                    final Attribute attribute = i.next();
                    item.put(attribute.getName().getLocalPart(),
                            attribute.getValue());
                }
                ctx.items.add(new MapListItem(item));
            } else {
                fail(event);
            }
            return ret;
        }

        @Override
        public XDRStateMachineParser parse(final EndElement event,
                final ParsingCtx ctx) {
            XDRStateMachineParser ret = null;
            if (QROW.equals(event.getName())) {
                ret = this;
            } else {
                ret = SCHEMA_OR_DATA;
            }
            return ret;
        }

    },
    /** z:row */
    ROW {
        @Override
        public XDRStateMachineParser parse(final StartElement event,
                final ParsingCtx ctx) {
            fail(event);
            return null;
        }

        @Override
        public XDRStateMachineParser parse(final EndElement event,
                final ParsingCtx ctx) {
            fail(event);
            return null;
        }

    },
    /** end state */
    END {
        @Override
        public XDRStateMachineParser parse(final StartElement event,
                final ParsingCtx ctx) {
            throw new IllegalStateException();
        }

        @Override
        public XDRStateMachineParser parse(final EndElement event,
                final ParsingCtx ctx) {
            throw new IllegalStateException();
        }

    };

    /** start element event */
    public abstract XDRStateMachineParser parse(StartElement event,
            ParsingCtx ctx);

    /** end element event */
    public abstract XDRStateMachineParser parse(EndElement event,
            ParsingCtx ctx);

    private static final QName QSCHEMA = new QName(
            "uuid:BDC6E3F0-6DA3-11d1-A2A3-00AA00C14882", "Schema");
    private static final QName QDATA = new QName(
            "urn:schemas-microsoft-com:rowset", "data");
    private static final QName QROW = new QName("#RowsetSchema", "row");

    /** throw exception */
    private static void fail(final StartElement event) throws IllegalStateException  {
        throw new IllegalStateException(
                "don't know what to do with start element " + event.getName());
    }

    /** throw exception */
    private static void fail(final EndElement event) throws IllegalStateException {
        throw new IllegalStateException(
                "don't know what to do with end element " + event.getName());
    }
}


/** Parsing context */
class ParsingCtx {
    /** items parsed  */
    public final List<ListItem> items = new ArrayList<ListItem>();
}