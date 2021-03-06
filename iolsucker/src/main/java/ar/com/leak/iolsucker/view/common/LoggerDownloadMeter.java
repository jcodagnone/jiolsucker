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
package ar.com.leak.iolsucker.view.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.leak.iolsucker.view.DownloadMeter;

/**
 * Logger download progress meter.
 *
 * @author <a href="mailto:brett@apache.org">Brett Porter</a>
 * @version $Id: ConsoleDownloadMeter.java,v 1.2 2004/07/23 04:40:06 brett Exp $
 */
public class LoggerDownloadMeter implements DownloadMeter {
    /** class logger */
    private final Logger logger = LoggerFactory.getLogger(LoggerDownloadMeter.class);
    /** constant: how many bytes are in a kilobyte */
    private static final int BYTES_IN_KILOBYTE = 1024;
    
    /** @see DownloadMeter.update(int,int) */
    public final void update(final int complete, final int total) {
        logger.info((complete / BYTES_IN_KILOBYTE) + "/" 
               + (total == 0 ? "?" : (total / BYTES_IN_KILOBYTE) + "K") + "\r");
    }

    /** @see DownloadMeter.finish(int) */
    public final void finish(final int total) {
        logger.info((total / BYTES_IN_KILOBYTE) + "K downloaded");
    }

}