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
package ar.com.leak.iolsucker.container.common;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.NullArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Codificador de passwords 
 *  
 * @author Juan F. Codagnone
 * @since Apr 4, 2005
 */
public final class PasswordCoder {
    /** Mao<String, AlgoCoder> */
    private static final Map<String, PasswordAlgoCoder> ALGOS = 
        new LinkedHashMap<String, PasswordAlgoCoder>();
    /** algoritmo preferido */
    private static final String PREFERED;
    
    static {
        PREFERED = "base64";
        ALGOS.put(PREFERED, new Base64PasswordCoder());
    }
    
    /**
     * Crea el PasswordCoder.
     */
    private PasswordCoder() {
        // utility class
    }
    
    /**
     * @param algorithm algoritmo a usaer
     * @param password password a codificar
     * @return retorna la password codificada con el algoritmo determiado
     */
    static String code(final String algorithm, final String password) {
        if(algorithm == null || password == null) {
            throw new NullArgumentException("ningun parametro puede ser nulo");
        }
     
        return getCoder(algorithm).code(password);
    }

    /**
     * @param algorithm algoritmo a usar
     * @param password password a decodificar
     * @return la password usando un algoritmo determinado
     */
    static String decode(final String algorithm, final String password) {
        if(algorithm == null || password == null) {
            throw new NullArgumentException("ningun parametro puede ser nulo");
        }
        
       return getCoder(algorithm).decode(password);
    }
    
    /**
     * @return el algoritmo de codificación default. No puede ser null ni vacio.
     */
    public static String getPreferedAlgorithm() {
        return PREFERED;
    }
    
    /**
     * @return los algoritmos disponibles para codificar la password
     */
    public static String[] getAlgorithms() {
        Object []array = ALGOS.keySet().toArray();
        String [] ret = new String[array.length];
        
        for(int i = 0; i < array.length; i++) {
            ret[i] = (String)array[i];
        }
        
        return ret;
    }
    
    /**
     * @param algorithm algorithm to search
     * @return the  PasswordAlgoCoder  for an algorithm name
     */
    private static PasswordAlgoCoder getCoder(final String algorithm) {
        PasswordAlgoCoder coder = ALGOS.get(algorithm);
        if(coder == null) {
            throw new IllegalArgumentException("algorithmo desconocido `"
                    + algorithm + "'");
        }
        
        return coder;
    }
}

/**
 * 
 * @author Juan F. Codagnone
 * @since Apr 4, 2005
 */
interface PasswordAlgoCoder {
    /**
     * @param s string to code
     * @return s coded
     */
    String code(String s);
    
    /**
     * @param s string to decode
     * @return s decoded
     */
    String decode(String s);
}

/**
 * BASE64 implementation for PasswordAlgoCoder 
 * @author Juan F. Codagnone
 * @since Apr 4, 2005
 */
class Base64PasswordCoder implements PasswordAlgoCoder {
    /** class logger */
    private final Logger logger = LoggerFactory.getLogger(Base64PasswordCoder.class);
    /** default encoding */
    private final String encoding = "UTF-8";
    
    /** @see PasswordAlgoCoder#code(String) */
    public String code(final String s) {
        String ret = null;
        
        try {
            ret = new String(Base64.encodeBase64(s.getBytes(encoding)),
                    encoding);
        } catch(final UnsupportedEncodingException e) {
            logger.error("codificando la password", e);
        }
        
        return ret;
    }

    /** @see PasswordAlgoCoder#decode(String) */
    public String decode(final String s) {
        String ret = null;
        
        try {
            ret = new String(Base64.decodeBase64(s.getBytes(encoding)), 
                    encoding);
        } catch(UnsupportedEncodingException e) {
            logger.error("decodificando la password", e);
        }
        
        return ret;
    }
}