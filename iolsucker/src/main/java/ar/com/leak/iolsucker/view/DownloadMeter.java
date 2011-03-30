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
package ar.com.leak.iolsucker.view;

/* ====================================================================
 *   Copyright 2001-2004 The Apache Software Foundation.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * ====================================================================
 */

/**
 * Download progress meter.
 *
 * @author <a href="mailto:brett@apache.org">Brett Porter</a>
 * @version $Id: DownloadMeter.java,v 1.2 2004/07/23 04:40:06 brett Exp $
 */
public interface DownloadMeter {
    /**
     * Update the progress bar.
     * @param complete amount complete in bytes
     * @param total the total in bytes, or 0 if unknown
     */
    void update(int complete, int total);

    /**
     * Finalise the progress bar.
     * @param total the total in bytes
     */
    void finish(int total);
}
