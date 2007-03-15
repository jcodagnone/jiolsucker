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

import ar.com.leak.iolsucker.view.DownloadMeter;


/**
 * Quiet implementation for <code>DownloadMeter</code>
 *
 * @author Juan F. Codagnone
 * @since Sep 6, 2005
 * @see ar.com.leak.iolsucker.view.DownloadMeter
 */
public final class NullDownloadMeter implements DownloadMeter {

    /** @see ar.com.leak.iolsucker.view.DownloadMeter#update(int, int) */
    public void update(final int complete, final int total) {
        // nothing to do! (we are shy)
    }

    /** @see ar.com.leak.iolsucker.view.DownloadMeter#finish(int) */
    public void finish(final int total) {
        // nothing to do! (we are shy)
    }
}
