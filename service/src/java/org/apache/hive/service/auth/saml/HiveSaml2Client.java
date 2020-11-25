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
import java.net.URI;
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

/**
 * HiveServer2's implementation of SAML2Client. We mostly rely on pac4j to do most of the
 * heavy lifting. This class implements the initialization logic of the underlying
 * {@link SAML2Client} using the HiveConf. Also, implements the generation of SAML
 * requests using HTTP-Redirect binding.
 * //TODO: Add support for HTTP-Post binding.
 */
public class HiveSaml2Client extends SAML2Client {

  private static final Logger LOG = LoggerFactory.getLogger(HiveSaml2Client.class);
  private static HiveSaml2Client INSTANCE;

  private HiveSaml2Client(SAML2Configuration saml2Configuration, String callbackUrl) {
    super(saml2Configuration);
    setCallbackUrl(callbackUrl);
    setName(HiveSaml2Client.class.getSimpleName());
    setStateGenerator(new HiveSamlRelayStateGenerator());
    LOG.info("Initializing the Saml2Client with callback url as {}", callbackUrl);
    init();
    //TODO handle the replayCache as described in http://www.pac4j.org/docs/clients/saml.html
  }

  private static String getCallBackUrl(HiveConf conf) throws Exception {
    URI callbackURI = HiveSamlUtils.getCallBackUri(conf);
    return callbackURI.toString();
  }

  public static synchronized HiveSaml2Client get(HiveConf conf)
      throws HttpSamlAuthenticationException {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    try {
      INSTANCE = new HiveSaml2Client(getSamlConfig(conf), getCallBackUrl(conf));
    } catch (Exception e) {
      throw new HttpSamlAuthenticationException("Could not instantiate SAML2.0 client",
          e);
    }
    return INSTANCE;
  }

  /**
   * Extracts the SAML specific configuration needed to initialize the SAML2.0 client.
   */
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
    saml2Configuration.setWantsAssertionsSigned(
        conf.getBoolVar(ConfVars.HIVE_SERVER2_SAML_WANT_ASSERTIONS_SIGNED));
    saml2Configuration
        .setAuthnRequestSigned(conf.getBoolVar(ConfVars.HIVE_SERVER2_SAML_SIGN_REQUESTS));
    return saml2Configuration;
  }

  /**
   * Generates a SAML request using the HTTP-Redirect Binding.
   */
  public void setRedirect(HttpServletRequest request, HttpServletResponse response)
      throws HttpSamlAuthenticationException {
    String responsePort = request.getHeader(HIVE_SAML_RESPONSE_PORT);
    if (responsePort == null || responsePort.isEmpty()) {
      throw new HttpSamlAuthenticationException("No response port specified");
    }
    LOG.debug("Request has response port set as {}", responsePort);
    Optional<RedirectionAction> redirect = getRedirectionAction(
        new JEEContext(request, response));
    if (!redirect.isPresent()) {
      throw new HttpSamlAuthenticationException("Could not get the redirect response");
    }
    response.setStatus(redirect.get().getCode());
    WithLocationAction locationAction = (WithLocationAction) redirect.get();
    try {
      String location = locationAction.getLocation();
      LOG.debug("Sending a redirect response to location = {}", location);
      response.sendRedirect(locationAction.getLocation());
    } catch (IOException e) {
      throw new HttpSamlAuthenticationException(e);
    }
  }

  /**
   * Given a response which may contain a SAML Assertion, validates it. If the validation
   * is successful, it extracts the nameId from the assertion which is used as the
   * identity of the end user.
   * @param request
   * @param response
   * @return the NameId as received in the assertion if the assertion was valid.
   * @throws HttpSamlAuthenticationException In case the assertition is not present or
   * is invalid.
   */
  public String validate(HttpServletRequest request, HttpServletResponse response)
      throws HttpSamlAuthenticationException {
    try {
      SAML2CredentialsExtractor credentialsExtractor = new SAML2CredentialsExtractor(
          this);
      Optional<SAML2Credentials> credentials = credentialsExtractor
          .extract(new JEEContext(request, response));
      if (!credentials.isPresent()) {
        throw new HttpSamlAuthenticationException("Credentials could not be extracted");
      }
      return credentials.get().getNameId().getValue();
    } catch (Exception ex) {
      throw new HttpSamlAuthenticationException("Could not validate the SAML response",
          ex);
    }
  }
}
