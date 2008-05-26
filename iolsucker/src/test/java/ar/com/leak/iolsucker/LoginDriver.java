/*
 * Copyright (c) 2008 Zauber S.A. -- All rights reserved
 */
package ar.com.leak.iolsucker;

import java.net.URL;

import junit.framework.TestCase;
import ar.com.leak.iolsucker.impl.common.RelativeLocationValidator;
import ar.com.leak.iolsucker.impl.common.login.SystemPropertyLoginInfo;
import ar.com.leak.iolsucker.impl.http.DefaultRequestFactory;
import ar.com.leak.iolsucker.impl.http.HTTPClient;
import ar.com.leak.iolsucker.impl.http.HTTPIolDao;
import ar.com.leak.iolsucker.impl.http.HTTPMaterialFactory;
import ar.com.leak.iolsucker.impl.http.NamingMapper;
import ar.com.leak.iolsucker.model.IolDAO;

/**
 * Sirve para probar que todavia nos podemos loguear en iol
 * 
 * @author Juan F. Codagnone
 * @since May 26, 2008
 */
public class LoginDriver extends TestCase {

    /** asd */
    public final void testFoo() throws Exception {
        final IolDAO iolDAO = new HTTPIolDao(
                new SystemPropertyLoginInfo(),
                new HTTPClient(new NamingMapper(new URL(
                        "http://silvestre.itba.edu.ar/itbaV/"))),
                new HTTPMaterialFactory(10, new DefaultRequestFactory(
                        new RelativeLocationValidator(),
                        new DefaultRequestFactory.DefaultMaterialFileFactory())));
        
        System.out.println(iolDAO.getUnreadNews());
    }
}
