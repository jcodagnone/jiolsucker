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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.leak.iolsucker.model.Material;


/**
 * Implementación trucha de <code>RepublishRepositoryStrategy</code>
 *
 * @author Juan F. Codagnone
 * @since Sep 6, 2005
 */
public class NullRepublishRepositoryStrategy 
          implements RepublishRepositoryStrategy {
    /** the logger ...*/
    private final Logger logger = LoggerFactory.getLogger(
            NullRepublishRepositoryStrategy.class);
    
    /** @see RepublishRepositoryStrategy#republish(Material, java.io.File) */
    public final void republish(final Material material, 
            final File existingFile) {

        logger.info("el archivo: " + material.getName() + " cambió, pero" 
                + " lo ignoro");
    }
}
