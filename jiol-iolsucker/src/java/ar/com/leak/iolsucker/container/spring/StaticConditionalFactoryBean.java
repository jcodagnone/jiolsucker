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
package ar.com.leak.iolsucker.container.spring;

import org.springframework.beans.factory.FactoryBean;


/**
 * Bean factory with a conditional. if the condition is <code>true</code>,
 * the trueBean is injected. else the falseBean
 *
 * @author Juan F. Codagnone
 * @since Aug 30, 2005
 */
public class StaticConditionalFactoryBean implements FactoryBean {
    /** el resultado es singleton ? */
    private boolean condition;
    /** bean to return if the condition is <code>true</code> */
    private Object trueBean;
    /** bean to return if the condition is <code>false</code> */
    private Object falseBean;
    
    /** @see org.springframework.beans.factory.FactoryBean#getObject() */
    public final Object getObject() throws Exception {
        return condition ? trueBean : falseBean;
    }

    /** @see org.springframework.beans.factory.FactoryBean#getObjectType() */
    public final Class getObjectType() {
        return condition ? (trueBean  == null ? null : trueBean.getClass())
                : (falseBean == null ? null : falseBean.getClass()); 
    }

    /** @see org.springframework.beans.factory.FactoryBean#isSingleton() */
    public final boolean isSingleton() {
        return true;
    }

    /**
     * @param condition the factory condition
     */
    public final void setCondition(final boolean condition) {
        this.condition = condition;
    }

    /**
     * @param falseBean the bean to return if the condition is 
     *                  <code>false</code>
     */
    public final void setFalseBean(final Object falseBean) {
        this.falseBean = falseBean;
    }

    /**
     * @param trueBean the bean to return if the condition is <code>true</code>
     */
    public final void setTrueBean(final Object trueBean) {
        this.trueBean = trueBean;
    }
}
