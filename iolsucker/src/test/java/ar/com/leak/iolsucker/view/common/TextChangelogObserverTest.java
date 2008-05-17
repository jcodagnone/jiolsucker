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

import ar.com.leak.iolsucker.view.Repository;
import ar.com.leak.iolsucker.view.Repository.ObservableActionEnum;

import junit.framework.TestCase;


/**
 * tests TextChangelogObserver
 *
 * @author Juan F. Codagnone
 * @since Aug 18, 2005
 * @link TextChangelogObserver
 */
public class TextChangelogObserverTest extends TestCase {

    /**
     * tests something...
     * 
     * @throws Exception on error
     */
    public final void testBulk() throws Exception {
        final File file = File.createTempFile("TextChangelogObersver_tmp", 
                "txt");
        final TextChangelogObserver o = 
             new TextChangelogObserver(file);
        final int numberToTest = 10;
        for(int i = 0; i < numberToTest; i++) {
            o.update(null, new Repository.ObservableAction("ble ble ble " + i,
                    ObservableActionEnum.NEW_FILE, new File("/bar/" + i)));
        }
        o.dispose();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String s;
        while((s = reader.readLine()) != null) {
            System.out.println(s);
        }
        
        file.delete();
        
        
    }
}
