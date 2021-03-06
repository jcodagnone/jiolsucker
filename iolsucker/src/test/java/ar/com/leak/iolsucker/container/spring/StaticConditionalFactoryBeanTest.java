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
package ar.com.leak.iolsucker.container.spring;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import junit.framework.TestCase;


/**
 * TODO Brief description.
 *
 * TODO Detail
 *
 * @author Juan F. Codagnone
 * @since Aug 30, 2005
 */
public class StaticConditionalFactoryBeanTest extends TestCase {

    /** @throws Exception on error */
    public final void testBean() throws Exception {
        final XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource(
                "ar/com/leak/iolsucker/container/spring/test.xml"));
        
        assertEquals(factory.getBean("test1"), "true");
        assertEquals(factory.getBean("test2"), "false");
    }
    
}
