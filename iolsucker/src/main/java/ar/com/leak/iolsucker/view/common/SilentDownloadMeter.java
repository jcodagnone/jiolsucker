package ar.com.leak.iolsucker.view.common;

import ar.com.leak.iolsucker.view.DownloadMeter;

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
 * Silent download progress meter.
 *
 * @author <a href="mailto:brett@apache.org">Brett Porter</a>
 * @version $Id: SilentDownloadMeter.java,v 1.2 2004/07/23 04:40:06 brett Exp $
 */
public class SilentDownloadMeter implements DownloadMeter {

    /** @see DownloadMeter.update(int,int) */
    public final void update(final int complete, final int total) {
        // This page left intentionally blank
    }

    /** @see DownloadMeter.finish(int) */
    public final void finish(final int total) {
        // This page left intentionally blank
    }

}