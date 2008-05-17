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
package ar.com.leak.iolsucker.view.common;

import java.io.*;
import java.net.MalformedURLException;
import java.security.SecureRandom;
import java.util.*;

import org.apache.commons.codec.binary.Base64;
import org.intabulas.sandler.Sandler;
import org.intabulas.sandler.SyndicationFactory;
import org.intabulas.sandler.builders.XPPBuilder;
import org.intabulas.sandler.elements.Feed;
import org.intabulas.sandler.exceptions.MarshallException;
import org.intabulas.sandler.serialization.SerializationException;
import org.intabulas.sandler.serialization.WriterSerializer;

import ar.com.leak.iolsucker.ProjectInfo;
import ar.com.leak.iolsucker.view.Repository;
import ar.com.leak.iolsucker.view.Repository.ObservableAction;


/**
 * Observar los cambios en el repositorio, y escribe un atom feed con dichos
 * cambios
 * 
 * TODO obtener los Ids del feed y entries sin hacer magia
 * 
 * @author Juan F. Codagnone
 * @since Aug 3, 2005
 */
public class AtomObserver implements Observer {
    /** feed */
    private final Feed feed;
    /** archivo de salida (puede ser null si outputStream no lo es) */
    private final File outputFile;
    
    /**
     * Crea el AtomObserver.
     *
     * @param secureRandom random generator
     * @param feedFile archivo donde vive el feed (lectura/escritura)
     * @param info  informacion del programa
     * @throws MarshallException on error
     * @throws IOException on error
     */
    public AtomObserver(final SecureRandom secureRandom, 
                        final File feedFile, final ProjectInfo info) 
                             throws MarshallException, IOException {
        if(feedFile.exists()) {
            InputStream stream = null;
            try {
                stream = new FileInputStream(feedFile);
                feed = Sandler.unmarshallFeed(stream, new XPPBuilder());
            } finally {
                if(stream != null) {
                    stream.close();
                }
            }
        } else {
            feed = SyndicationFactory.createFeed(SyndicationFactory
                    .createPerson(info.getProjectName(), "", ""), "", 
                    SyndicationFactory.createAlternateLink(""), new Date());
            feed.setTitle("jiol changelog");
            
            final int idLength = 33;
            final byte [] bytes = new byte[idLength];
            secureRandom.nextBytes(bytes);
            final String id = info.getProjectName() + "-"
                                       + new String(Base64.encodeBase64(bytes));
            feed.setId(id);
        }
    
        outputFile = feedFile;
    }
    
    
    /** @see Observer#update(java.util.Observable, java.lang.Object)*/
    public final void update(final Observable o, final Object arg) {
        //final Repository repository = (Repository)o;
        final Repository.ObservableAction action = (ObservableAction)arg;
        
        try {
            feed.addEntry(SyndicationFactory.createEntry(
                    SyndicationFactory.createEscapedContent(action.getMsg()),
                    action.getType() + ": " + action.getFile().getPath(),
                    feed.getId() + "-" + (feed.getEntryCount()  
                        + GregorianCalendar.getInstance().getTimeInMillis()),
                        SyndicationFactory.createAlternateLink(action.getFile().
                        toURL().toExternalForm()),
                    new Date(), new Date()));
        } catch(final MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /** cierra todos los recursos abiertos */
    public final void dispose() {
        try { 
            final File tmpFile = File.createTempFile("changelog.", "atom.xml", 
                    outputFile.getParentFile());
            OutputStream outStream = new FileOutputStream(tmpFile);
            write(outStream);
            outStream.close();
            // i wish i had link(2)
            final File oldFile = new File("changelog.atom.old.xml");
            if(outputFile.exists()) {
                oldFile.delete();
                outputFile.renameTo(oldFile);
            }
            
            if(tmpFile.renameTo(outputFile)) {
                oldFile.delete();
            }
        } catch(final Exception e) {
            throw new RuntimeException(e);
        }    
    }

    /**
     * Escribe el atom feed al stream out
     * 
     * @param out stream de salida
     * @throws SerializationException en caso de error
     */
    private void write(final OutputStream out) 
        throws SerializationException {
        new WriterSerializer(new OutputStreamWriter(out)).serialize(feed);
    }
}

