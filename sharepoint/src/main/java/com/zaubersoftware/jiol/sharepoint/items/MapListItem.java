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

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.lang.UnhandledException;
import org.apache.commons.lang.Validate;

import ar.com.zauber.commons.dao.Transformer;

/**
 * ListItem backed on a {@link Map}
 *
 * @author Juan F. Codagnone
 * @since Mar 8, 2011
 */
public class MapListItem implements ListItem {
    private final Map<String, String> map;
    private final Map<Class<?>, Transformer<String, ?>> transformers;

    /** Creates the MapListItem. */
    public MapListItem(final Map<String, String> map) {
        this(map, transformer());
    }
    
    /** Creates the MapListItem. */
    public MapListItem(final Map<String, String> map,
            final Map<Class<?>, Transformer<String, ?>> transformers) {
        Validate.notNull(map);
        Validate.notNull(transformers);
        
        this.map = map;
        this.transformers = transformers;
    }
    
    @Override
    public final boolean contains(final String key) {
        return map.containsKey(key);
    }

    @Override
    public final String get(final String key) {
        return map.get(key);
    }

    @Override
    public final <T> T get(final String key, final Class<T> clazz) {
        final Transformer t = transformers.get(clazz);
        if(t == null) {
            throw new IllegalArgumentException(String.format(
                    "Can't transform key %s to class %s: Unknown class",
                    key, clazz.getClass().getSimpleName()));
        }
        
        return (T) t.transform(get(key));
    }
    
    @Override
    public final String toString() {
        return map.toString();
    }
    
    /** crea convertidores */
    public static Map<Class<?>, Transformer<String, ?>> transformer() {
        try {
            final DatatypeFactory datatype = DatatypeFactory.newInstance();
            final Map<Class<?>, Transformer<String, ?>> ret = 
                new HashMap<Class<?>, Transformer<String, ?>>();
            
            ret.put(Date.class, new Transformer<String, Date>() {
                @Override
                public Date transform(final String input) {
                    
                    return datatype.newXMLGregorianCalendar(input).toGregorianCalendar().getTime();
                }
            });
            ret.put(Long.class, new Transformer<String, Long>() {
                @Override
                public Long transform(final String input) {
                    return Long.parseLong(input);
                }
            });
            ret.put(URI.class, new Transformer<String, URI>() {
                @Override
                public URI transform(final String input) {
                    return URI.create(input);
                }
            });
            return ret;
        } catch (DatatypeConfigurationException e) {
            throw new UnhandledException(e);
        }
    }
}
