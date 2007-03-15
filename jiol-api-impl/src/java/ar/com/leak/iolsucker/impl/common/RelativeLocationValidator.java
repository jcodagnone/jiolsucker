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
package ar.com.leak.iolsucker.impl.common;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @see #isValid(String)  
 * @author Juan F. Codagnone
 * @since Apr 30, 2005
 */
public class RelativeLocationValidator implements Validator {
    /** class logger */
    private Log logger = LogFactory.getLog(RelativeLocationValidator.class);
    /** codec helper */
    private final URLCodec urlCodec = new URLCodec();
    
    /**
     * @param path path del archivo a testear
     * @return <code>true</code> si el path es inseguro (contiene caracteres 
     * para irse fuera del sandbox)
     */
    public final boolean isValid(final String path) {
        boolean ret = false;
        try {
            final String decoded = urlCodec.decode(path);
            // TODO apesta, armar una regex o una forma mas generica
            if(decoded.indexOf("/../") > -1 
                    || decoded.indexOf("\\..\\") > -1 
                    || decoded.startsWith("../")  
                    || decoded.startsWith("..\\")
                    || decoded.endsWith("/..")
                    || decoded.endsWith("\\..")
                    || decoded.equals("..")) {
                ret = false;
            } else {
                ret = true;
            }
        } catch(DecoderException e) {
            logger.warn("sanitaizing", e);
        }
        
        return ret;
    }

    /** @see Validator#isValid(String) */
    public final boolean isValid(final Object object) {
        boolean ret;
        
        if(object instanceof String) {
            ret = isValid((String)object);
        } else {
            ret = false;
        }
        
        return ret;
    }
}
