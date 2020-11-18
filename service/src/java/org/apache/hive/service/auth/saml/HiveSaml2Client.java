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

import static org.apache.hive.service.auth.saml.HiveSamlUtils.HIVE_SAML_RESPONSE_PORT;
import static org.opensaml.saml.common.xml.SAMLConstants.SAML2_POST_BINDING_URI;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.conf.HiveConf.ConfVars;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.pac4j.core.context.JEEContext;
import org.pac4j.core.exception.http.RedirectionAction;
import org.pac4j.core.exception.http.WithLocationAction;
import org.pac4j.saml.client.SAML2Client;
import org.pac4j.saml.config.SAML2Configuration;
import org.pac4j.saml.credentials.SAML2Credentials;
import org.pac4j.saml.credentials.extractor.SAML2CredentialsExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HiveSaml2Client extends SAML2Client {
  private static final Logger LOG = LoggerFactory.getLogger(HiveSaml2Client.class);
  private static HiveSaml2Client INSTANCE;

  private HiveSaml2Client(SAML2Configuration saml2Configuration, String callbackUrl) {
    super(saml2Configuration);
    //TODO what is this? should be configure this?
    LOG.info("VIHANG-DEBUG: Starting the SAML client with callback URL as {}", callbackUrl);
    setCallbackUrl(callbackUrl);
    setName(HiveSaml2Client.class.getSimpleName());
    init();
    //TODO handle the replayCache as described in http://www.pac4j.org/docs/clients/saml.html
  }

  private static String getCallBackUrl(HiveConf conf) {
    int portNum = conf.getIntVar(ConfVars.HIVE_SERVER2_THRIFT_HTTP_PORT);
    String ssoPath = conf.getVar(ConfVars.HIVE_SERVER2_SAML_CALLBACK_HTTP_PATH);
    //TODO(Vihang) determine scheme and hostname
    return "http://localhost:"+portNum + ssoPath;
  }

  public static synchronized HiveSaml2Client get(HiveConf conf) throws HiveSamlException {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    try {
      INSTANCE = new HiveSaml2Client(getSamlConfig(conf), getCallBackUrl(conf));
    } catch (Exception e) {
      throw new HiveSamlException("Could not instantiate SAML2.0 client", e);
    }
    return INSTANCE;
  }

  private static SAML2Configuration getSamlConfig(HiveConf conf) {
    SAML2Configuration saml2Configuration = new SAML2Configuration(
        conf.get(ConfVars.HIVE_SERVER2_SAML_KEYSTORE_PATH.varname),
        conf.get(ConfVars.HIVE_SERVER2_SAML_KEYSTORE_PASSWORD.varname),
        conf.get(ConfVars.HIVE_SERVER2_SAML_PRIVATE_KEY_PASSWORD.varname),
        conf.get(ConfVars.HIVE_SERVER2_SAML_IDP_METADATA.varname));
    saml2Configuration
        .setAuthnRequestBindingType(SAMLConstants.SAML2_REDIRECT_BINDING_URI);
    saml2Configuration.setResponseBindingType(SAML2_POST_BINDING_URI);
    saml2Configuration
        .setServiceProviderEntityId(conf.get(ConfVars.HIVE_SERVER2_SAML_SP_ID.varname));
    saml2Configuration.setWantsAssertionsSigned(true);
    saml2Configuration.setAuthnRequestSigned(false);
    return saml2Configuration;
  }

  public void setRedirect(HttpServletRequest request, HttpServletResponse response)
      throws HttpSamlAuthenticationException {
    String responsePort = request.getHeader(HIVE_SAML_RESPONSE_PORT);
    if (responsePort == null || responsePort.isEmpty()) {
      throw new HttpSamlAuthenticationException("No response port specified");
    }
    Optional<RedirectionAction> redirect = getRedirectionAction(
        new JEEContext(request, response));
    if (!redirect.isPresent()) {
      throw new HttpSamlAuthenticationException("Could not get the redirect response");
    }
    response.setStatus(redirect.get().getCode());
    WithLocationAction locationAction = (WithLocationAction) redirect.get();
    try {
      String location = locationAction.getLocation();
      LOG.info("VIHANG-DEBUG: location = {}", location);
      response.sendRedirect(locationAction.getLocation());
    } catch (IOException e) {
      throw new HttpSamlAuthenticationException(e);
    }
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
          this);
      Optional<SAML2Credentials> credentials = credentialsExtractor
          .extract(new JEEContext(request, response));
      //TODO(Vihang) find a better way to distinguish a redirect case
      if (!credentials.isPresent()) {
        throw new HttpSamlAuthenticationException("Credentials could not be extracted");
      }
      return credentials.get().getNameId().getValue();
    } catch (Exception ex) {
      LOG.error("Error received while processing the request", ex);
      throw new HttpSamlRedirectException("Credentials could not be extracted", ex);
    }
  }
}
