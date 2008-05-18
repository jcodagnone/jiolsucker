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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.SerializationException;

import ar.com.leak.iolsucker.ProjectInfo;
import ar.com.leak.iolsucker.view.Repository;
import ar.com.leak.iolsucker.view.Repository.ObservableAction;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.SyndFeedOutput;


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
    private SyndFeed feed;
    /** archivo de salida (puede ser null si outputStream no lo es) */
    private final File outputFile;
    
    /**
     * Crea el AtomObserver.
     *
     * @param secureRandom random generator
     * @param feedFile archivo donde vive el feed (lectura/escritura)
     * @param info  informacion del programa
     * @throws IOException on error
     * @throws FeedException on error 
     */
    public AtomObserver(final SecureRandom secureRandom, 
                        final File feedFile, final ProjectInfo info) 
                             throws IOException, FeedException {
        boolean newFile = true;
        if(feedFile.exists()) {
            try {
                feed = new SyndFeedInput().build(feedFile);
                newFile = false;
            } catch (Throwable t) {
                // nothing todo
            }
        } 
        
        
        if(newFile) {
            feed = new SyndFeedImpl();
            feed.setFeedType("atom_1.0");
            feed.setTitle("jiol changelog");
            feed.setAuthor("jiolsucker using rome v0.9");
            feed.setEntries(new ArrayList());
            final int idLength = 33;
            final byte [] bytes = new byte[idLength];
            secureRandom.nextBytes(bytes);
            final String id = info.getProjectName() + "-"
                                       + new String(Base64.encodeBase64(bytes));
            feed.setUri(id);
        }
    
        outputFile = feedFile;
    }
    
    
    /** @see Observer#update(java.util.Observable, java.lang.Object)*/
    public final void update(final Observable o, final Object arg) {
        //final Repository repository = (Repository)o;
        final Repository.ObservableAction action = (ObservableAction)arg;
        
        final SyndEntry entry = new SyndEntryImpl();
        entry.setTitle(action.getMsg());
        entry.setPublishedDate(new Date());
        try {
            entry.setLink(action.getFile().toURL().toExternalForm());
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }
        final com.sun.syndication.feed.synd.SyndContent content = 
            new com.sun.syndication.feed.synd.SyndContentImpl();
        content.setValue(action.getType() + ": " + action.getFile().getPath());
        content.setType("html");
        final List contents = new ArrayList();
        contents.add(content);
        entry.setDescription(content);
        entry.setContents(contents);
        entry.setPublishedDate(new Date());
        entry.setContents(contents);
        feed.getEntries().add(entry);
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
    private void write(final OutputStream out) {
        final com.sun.syndication.io.SyndFeedOutput output = 
            new com.sun.syndication.io.SyndFeedOutput();
        try {
            output.output(feed, new OutputStreamWriter(out));
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
