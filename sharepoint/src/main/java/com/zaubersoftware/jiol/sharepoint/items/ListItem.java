/*
 *  Copyright 2011 Juan F. Codagnone <juam at users dot sourceforge dot net>
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
package com.zaubersoftware.jiol.sharepoint.items;

/**
 * Sharepoint List Item
 * http://msdn.microsoft.com/en-us/library/ms774821(v=office.12).aspx
 * 
 * @author Juan F. Codagnone
 * @since Mar 8, 2011
 */
public interface ListItem {

    /** @return true if the item contains the property */
    boolean contains(String key); 
    
    /** @return item property */
    String get(String key);
    
    /** @return item property typed */
    <T> T get(String key, Class<T> clazz);
}
