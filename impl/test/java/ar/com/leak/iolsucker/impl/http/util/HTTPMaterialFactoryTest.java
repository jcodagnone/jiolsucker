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
package ar.com.leak.iolsucker.impl.http.util;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import junit.framework.TestCase;
import ar.com.leak.iolsucker.impl.common.RelativeLocationValidator;
import ar.com.leak.iolsucker.impl.http.*;
import ar.com.leak.iolsucker.impl.mock.MockHTTPClient;
import ar.com.leak.iolsucker.model.Material;

/**
 * testea el<code>ar.com.leak.iolsucker.impl.http.HTTPMaterialFactory</code>
 * 
 * @see ar.com.leak.iolsucker.impl.http.HTTPMaterialFactory
 * @author Juan F. Codagnone
 * @since Apr 13, 2005
 */
public class HTTPMaterialFactoryTest extends TestCase {


    /**
     * @throws IOException on error
     */
    public final void testUser() throws IOException {
        final Map<String, URL> map = new HashMap<String, URL>();
        final ClassLoader loader = getClass().getClassLoader();
        final URL blank = MockHTTPClient.getResourceURL(loader, 
                "material/blank.txt");
        
        map.put("newmaterialdid.asp", MockHTTPClient.getResourceURL(loader,
                "material/user.html"));
        map.put("newmaterialdid.asp?fid=25138", 
                MockHTTPClient.getResourceURL(loader, "material/user1.html"));
        map.put("newmaterialdid.asp?fid=28084", blank);
        map.put("newmaterialdid.asp?fid=31617", 
                MockHTTPClient.getResourceURL(loader, "material/user2.html"));
        map.put("newmaterialdid.asp?fid=31835", blank);
        map.put("newmaterialdid.asp?fid=31822", blank);
        map.put("newmaterialdid.asp?fid=31821", blank);
        map.put("newmaterialdid.asp?fid=31834", blank);
        map.put("newmaterialdid.asp?fid=31823", blank);
        map.put("newmaterialdid.asp?fid=31836", blank);
        
        final HTTPClient client = new MockHTTPClient(new NamingMapper(new URL(
                "http://foo/bar")), map);

        final String [] expectedFactoryRequest = {
                "newmaterialdid.asp",
                "newmaterialdid.asp?fid=25138",
                "newmaterialdid.asp?fid=28084",
                "newmaterialdid.asp?fid=31617",
                "newmaterialdid.asp?fid=31835",
                "newmaterialdid.asp?fid=31822",
                "newmaterialdid.asp?fid=31821",
                "newmaterialdid.asp?fid=31834",
                "newmaterialdid.asp?fid=31823",
                "newmaterialdid.asp?fid=31836",
        };
        final String [] expectedFileNames = { 
                "25140", "25139",// "16680"
        };
        final String [] expectedFolderNames = { 
            "Acreditacion CONEAU", "Boletin General 2005", "Disposiciones ITBA",
            "Disposiciones ITBA/ACREDITACIONES",
            "Disposiciones ITBA/CREACIONES",
            "Disposiciones ITBA/DESIGNACIONES",
            "Disposiciones ITBA/INFRAESTRUCTURA",
            "Disposiciones ITBA/NORMAS Y PROCEDIMIENTOS",
            "Disposiciones ITBA/VARIOS",
        };

        
        final Set<String> expectedFiles = new HashSet<String>(Arrays.asList(
                expectedFileNames));
        final Set<String> expectedFolders = new HashSet<String>(Arrays.asList(
                expectedFolderNames));

        testMaterialFactory(client, expectedFactoryRequest, expectedFiles,
                expectedFolders);
    }
    
    /**
     * testeo que intenta corroborar que la clase que parsea links de material
     * didactico haga un buen trabajo. Dada una página, se explicita los links
     * que se deben segir, y los resultados que se deben obtener, y se verifica
     * que todos se cumplan (que no haya otros, y que no falten).
     * 
     * @param client
     *            cliente http
     * @param expectedFactoryRequestsSz
     *            Requests que espero que le hagan al HttpMaterialFactory
     * @param expectedFiles
     *            Nombres de los archivos que espero que me retorne
     * @param expectedFolders
     *            Nombres de las carpetas que espero que me retornen
     * 
     * @throws IOException on error
     */
    public final void testMaterialFactory(final HTTPClient client,
            final String [] expectedFactoryRequestsSz, final Set expectedFiles,
            final Set expectedFolders) throws IOException {

        /*
         * como hay directorios, el HTTPMaterialFactory en su siguiente
         * iteración debera hacer algunos requests:
         */
        final Collection<Material> emptyCollection = new ArrayList<Material>();
        final Map<String, Collection<Material>> expectedFactoryRequests = 
            new HashMap<String, Collection<Material>>();
        for(int i = 0; i < expectedFactoryRequestsSz.length; i++) {
            expectedFactoryRequests.put(expectedFactoryRequestsSz[i],
                    emptyCollection);

        }

        final MockRequestFactory requestFactory = 
            new MockRequestFactory(new RelativeLocationValidator(), 
                    expectedFactoryRequests);
        final Collection ret = new HTTPMaterialFactory(1, requestFactory).
                           getMaterial(client);

        assertTrue("se hizo mal la recursión", requestFactory
                .allURIsWereVisited());

        for(Iterator i = ret.iterator(); i.hasNext();) {
            Material material = (Material)i.next();

            checkIfWasExpected(material.isFolder() ? expectedFolders
                    : expectedFiles, material);
        }
        assertTrue("archivos esperados no se visitaron!: " + expectedFiles,
                expectedFiles.size() == 0);
        assertTrue("carpetas esperados no se visitaron!", expectedFolders
                .size() == 0);
    }

    /**
     * @param material material que se quiere testear si pertenece a expected
     * @param expected set de Strings con paths esperados que se hagan en el 
     *                 como request al servidor
     */
    private void checkIfWasExpected(final Set expected, 
            final Material material) {
        String s = material.toString();

        if(expected.contains(s)) {
            expected.remove(s);
        } else {
            assertTrue("Material `" + material.toString() + "'no esperada",
                    false);
        }
    }

}
