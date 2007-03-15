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
package ar.com.leak.iolsucker.impl.common;

import junit.framework.TestCase;

/**
 * Unit test for ar.com.leak.iolsucker.impl.common.RelativeLocationValidator
 * 
 * @author Juan F. Codagnone
 * @since Jul 6, 2005
 * @link ar.com.leak.iolsucker.impl.common.RelativeLocationValidator
 */
public class RelativeLocationValidatorTest extends TestCase {

    /**
     * testea el método testUnsafePath
     */
    public final void testUnsafePath() {
        final RelativeLocationValidator validator = 
            new RelativeLocationValidator();
        final String []unsafePaths = {
                "..",
                "%2e%2E",
                "..\\",
                "../",
                "../pepe/pepo",
                "..%2Fpepe/pepo",
                "%2E.%2Fpepe/pepo", 
                "..\\pepe\\pepo",
                "..\\pepe\\pepo\\..",
                "..\\pepe\\pepo\\..",
                "pepe\\pepo/..",
                "pepe\\pepo\\..",
                "pepe\\pepo/%2e.",
                "../../../../../../../../../../../../proc/version",
                "test/../../other", "test/../../other/..",
                "test/../../other/../../../../../..",
                "test/../../other/../../../../../../aaaaa",
        };
        final String []safePaths = {
                "test", 
                "test/test1/test2/./pepe",
                "test/test1/test2/%2e/pepe",
                "test/test1/test2/..pepe",
                "test/test1/test2/%2e%2epepe",
                "test/test1/test2/....pepe",
        };
        for(int i = 0; i < safePaths.length; i++) {
            final String test = safePaths[i];
            assertTrue(validator.isValid(test));
        }
        
        for(int i = 0; i < unsafePaths.length; i++) {
            final String test = unsafePaths[i];
            assertFalse(validator.isValid(test));
        }
    }
}
