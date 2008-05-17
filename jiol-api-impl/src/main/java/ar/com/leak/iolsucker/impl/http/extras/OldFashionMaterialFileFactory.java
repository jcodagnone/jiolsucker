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
package ar.com.leak.iolsucker.impl.http.extras;

import ar.com.leak.iolsucker.impl.http.HTTPClient;
import ar.com.leak.iolsucker.impl.http.DefaultRequestFactory
     .MaterialFileFactory;
import ar.com.leak.iolsucker.model.Material;


/**
 * Factory que crea OldFashionHttpMaterialFile
 *
 * @author Juan F. Codagnone
 * @since Aug 23, 2005
 * @see ar.com.leak.iolsucker.impl.http.extras.OldFashionHttpMaterialFile
 */
public class OldFashionMaterialFileFactory implements MaterialFileFactory {
    /**
     * @see MaterialFileFactory#createMaterialFile(HTTPClient, 
     *   java.lang.String, int)
     */
    public final Material createMaterialFile(final HTTPClient client, 
            final String parent, final int fid) {
        
        return new OldFashionHttpMaterialFile(client, parent, fid);
    }

}
