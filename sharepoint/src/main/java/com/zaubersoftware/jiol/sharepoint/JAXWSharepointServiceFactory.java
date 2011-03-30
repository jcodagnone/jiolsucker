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
package com.zaubersoftware.jiol.sharepoint;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.apache.commons.lang.UnhandledException;
import org.apache.commons.lang.Validate;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.http.Header;
import org.apache.http.HttpVersion;
import org.apache.http.client.params.ClientParamBean;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParamBean;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParamBean;

import ar.com.leak.iolsucker.model.LoginInfo;
import ar.com.zauber.leviathan.api.URIFetcher;
import ar.com.zauber.leviathan.impl.httpclient.HTTPClientURIFetcher;
import ar.com.zauber.leviathan.impl.httpclient.charset.FixedCharsetStrategy;

import com.microsoft.schemas.sharepoint.soap.Authentication;
import com.microsoft.schemas.sharepoint.soap.AuthenticationSoap;
import com.microsoft.schemas.sharepoint.soap.LoginErrorCode;
import com.microsoft.schemas.sharepoint.soap.LoginResult;

/**
 * Maneja autenticacion de servicios JAX-WS
 * 
 * @author Juan F. Codagnone
 * @since Mar 8, 2011
 */
public class JAXWSharepointServiceFactory implements SharepointServiceFactory {
    private final URIFetcher uriFetcher;
    private final Map<?, ?> cookies;
    
    /**
     * Creates the JAXWSharepointServiceFactory 
     *
     */
    public JAXWSharepointServiceFactory(final URISharepointStrategy uriStrategy, 
            final LoginInfo credentialsProvider)  {
        Validate.notNull(uriStrategy);
        Validate.notNull(credentialsProvider);
        
        AuthenticationSoap authentication;
        try {
            authentication = new Authentication(uriStrategy.getUriForService(
                    Authentication.class).toURL()).getAuthenticationSoap();
        } catch (MalformedURLException e) {
            throw new UnhandledException(e);
        }
        configureBinding((BindingProvider) authentication);
        final LoginResult login = authentication.login(credentialsProvider.getUsername(), 
                                                       credentialsProvider.getPassword());
        if(login.getErrorCode() != LoginErrorCode.NO_ERROR) {
            throw new IllegalArgumentException("logging in: " + login.getErrorCode());
        }
        final HTTPConduit authenticationHTTP = (HTTPConduit) 
                ClientProxy.getClient(authentication).getConduit();
        cookies = authenticationHTTP.getCookies();
        
        uriFetcher = getURIFetcher(cookies);
    }
    
    @Override
    public final void configureService(final BindingProvider provider) {
        configureCookies(provider, cookies); 
    }


    /** @return a {@link URIFetcher} authenticated that will be used to download files */
    private static URIFetcher getURIFetcher(final Map<?, ?> cookies) {
        final HttpParams params = new BasicHttpParams();
        final HttpProtocolParamBean protocol = new HttpProtocolParamBean(params);
        protocol.setContentCharset("iso-8859-1");
        // La gente de sistemas del itba tiende a banear jiolsucker, y otros nombres lindos
        protocol.setUserAgent("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1)");
        protocol.setVersion(HttpVersion.HTTP_1_1);
        
        final HttpConnectionParamBean http = new HttpConnectionParamBean(params);
        http.setConnectionTimeout(10000);
        http.setSoTimeout(20000);
        http.setLinger(-1);
        http.setStaleCheckingEnabled(true);
        http.setTcpNoDelay(true);
        
        final ClientParamBean client = new ClientParamBean(params);
        client.setHandleRedirects(false);
        client.setCookiePolicy("compatibility");
        final Object o = cookies.values().iterator().next();
        final String value;
        final String name;
        try {
            final Field v = o.getClass().getDeclaredField("value");
            v.setAccessible(true);
            value = (String) v.get(o);
            final Field n = o.getClass().getDeclaredField("name");
            n.setAccessible(true);
            name = (String) n.get(o);
        } catch(final Exception e) {
            throw new UnhandledException(e);
        }
        client.setDefaultHeaders(Arrays.asList(new Header[] { new BasicHeader(
                "Cookie", name + "=" + value) }));
        
        return new HTTPClientURIFetcher(new DefaultHttpClient(params),
                new FixedCharsetStrategy("utf-8"));
    }
    

    /** configure provider settings */
    private static void configureBinding(final BindingProvider provider) {
        provider.getRequestContext().put(BindingProvider.SESSION_MAINTAIN_PROPERTY, Boolean.TRUE);

    }

    /**
     * Esto es un hack de CXF (no es valido para otros proveedores JAXWS) pero funciona.
     * Setea las cookies de autenticación a los proveedores que se crean.
     */
    private static void configureCookies(final BindingProvider provider, final Map cookies) {
        configureBinding(provider);
        final HTTPConduit conduit = (HTTPConduit) ClientProxy.getClient(provider).getConduit();
        conduit.getCookies().putAll(cookies);
    }


    @Override
    public final URIFetcher getUriFetcher() {
        return uriFetcher;
    }
    
}
