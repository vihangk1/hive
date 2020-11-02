/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hive.service.auth.saml;

import static org.opensaml.saml.common.xml.SAMLConstants.SAML2_POST_BINDING_URI;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.hadoop.conf.Configuration;
import org.opensaml.saml.common.SAMLException;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml2.core.AuthnRequest;
import org.opensaml.xmlsec.signature.J;
import org.pac4j.core.context.JEEContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.exception.http.RedirectionAction;
import org.pac4j.core.exception.http.WithLocationAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.saml.client.SAML2Client;
import org.pac4j.saml.config.SAML2Configuration;
import org.pac4j.saml.context.SAML2MessageContext;
import org.pac4j.saml.credentials.SAML2Credentials;
import org.pac4j.saml.credentials.extractor.SAML2CredentialsExtractor;
import org.pac4j.saml.profile.api.SAML2ResponseValidator;
import org.pac4j.saml.redirect.SAML2RedirectionActionBuilder;
import org.pac4j.saml.sso.impl.SAML2AuthnRequestBuilder;
import org.pac4j.saml.transport.Pac4jSAMLResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HiveSamlClient {

  private static final Logger LOG = LoggerFactory.getLogger(HiveSamlClient.class);
  private static HiveSamlClient samlClient;
  private final SAML2Configuration saml2Configuration;
  private final SAML2Client saml2Client;

  public static synchronized HiveSamlClient get(Configuration conf)
      throws HiveSamlException {
    if (samlClient != null) {
      return samlClient;
    }
    samlClient = new HiveSamlClient(conf);
    return samlClient;
  }

  private HiveSamlClient(Configuration conf) throws HiveSamlException {
    // TODO pass these via hive-site.xml
    try {
      saml2Configuration = new SAML2Configuration("file:///tmp/samlKeystore.jks",
          "pac4j-demo-passwd",
          "pac4j-demo-passwd",
          "file:///tmp/idp-metadata-6.xml");
      saml2Configuration
          .setAuthnRequestBindingType(SAMLConstants.SAML2_REDIRECT_BINDING_URI);
      saml2Configuration.setResponseBindingType(SAML2_POST_BINDING_URI);
      saml2Configuration.setServiceProviderEntityId("hs2-saml");
      saml2Configuration.setWantsAssertionsSigned(true);
      saml2Configuration.setAuthnRequestSigned(true);
      saml2Client = new SAML2Client(saml2Configuration);
      //TODO what is this?
      saml2Client.setCallbackUrl("http://localhost:10001/cliservice");
      saml2Client.setName(HiveSamlClient.class.getSimpleName());
      saml2Client.init();
      //TODO handle the replayCache as described in http://www.pac4j.org/docs/clients/saml.html
    } catch (Exception ex) {
     throw new HiveSamlException(ex);
    }
  }

  public void getRedirectUrl(HttpServletRequest request, HttpServletResponse response)
      throws HttpSamlAuthenticationException {
    /*SAML2RedirectionActionBuilder redirectionActionBuilder = new SAML2RedirectionActionBuilder(
        saml2Client);
    Optional<RedirectionAction> redirect = redirectionActionBuilder
        .getRedirectionAction(new JEEContext(request, response));*/
    Optional<RedirectionAction> redirect = saml2Client
        .getRedirectionAction(new JEEContext(request, response));
    if (!redirect.isPresent()) {
      throw new HttpSamlAuthenticationException("Could not get the redirect response");
    }
    response.setStatus(redirect.get().getCode());
    WithLocationAction locationAction = (WithLocationAction) redirect.get();
    try {
      String location = locationAction.getLocation();
      LOG.info("VIHANG-DEBUG: location = {}", location);
      URL redirectUrl = new URL(location);
      response.sendRedirect(locationAction.getLocation());
    } catch (IOException e) {
      throw new HttpSamlAuthenticationException(e);
    }
  }

  public String getSSOUrl(HttpServletRequest request, HttpServletResponse response) {
    WebContext webContext = new JEEContext(request, response);
    final SAML2MessageContext context = saml2Client.getContextProvider()
        .buildContext(webContext);
    final String relayState = saml2Client.getStateGenerator().generateValue(webContext);
    SAML2AuthnRequestBuilder requestBuilder = new SAML2AuthnRequestBuilder(
        saml2Client.getConfiguration());
    final AuthnRequest authnRequest = requestBuilder.build(context);
    saml2Client.getProfileHandler().send(context, authnRequest, relayState);
    final Pac4jSAMLResponse adapter = context
        .getProfileRequestContextOutboundMessageTransportResponse();
    return adapter.getRedirectUrl();
  }

  public String validate(HttpServletRequest request, HttpServletResponse response)
      throws HttpSamlAuthenticationException {
    try {
      /*SAML2ResponseValidator responseValidator = saml2Client.getAuthnResponseValidator();
      final SAML2MessageContext context = saml2Client.getContextProvider()
          .buildContext(new JEEContext(request, response));
      Credentials samlCredentials = responseValidator.validate(context);
      return samlCredentials.getUserProfile().getUsername();*/
      SAML2CredentialsExtractor credentialsExtractor = new SAML2CredentialsExtractor(
          saml2Client);
      Optional<SAML2Credentials> credentials = credentialsExtractor
          .extract(new JEEContext(request, response));
      if (!credentials.isPresent()) {
        throw new HttpSamlAuthenticationException("Credentials could not be extracted");
      }
      return credentials.get().getNameId().getValue();
    } catch (Exception ex) {
      LOG.error("Error received while processing the request", ex);
      throw new HttpSamlRedirectException("Credentials could not be extracted", ex);
    }
  }

  public boolean needsRedirect(HttpServletRequest request) {
    // TODO implement look for SAML assertion here
    return true;
  }
}