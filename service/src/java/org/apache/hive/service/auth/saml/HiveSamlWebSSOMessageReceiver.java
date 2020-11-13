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

import org.pac4j.core.context.WebContext;
import org.pac4j.saml.exceptions.SAMLException;
import org.pac4j.saml.profile.api.SAML2ResponseValidator;
import org.pac4j.saml.sso.impl.SAML2WebSSOMessageReceiver;
import org.pac4j.saml.transport.AbstractPac4jDecoder;
import org.pac4j.saml.transport.Pac4jHTTPPostDecoder;
import org.pac4j.saml.util.Configuration;

public class HiveSamlWebSSOMessageReceiver extends SAML2WebSSOMessageReceiver {

  public HiveSamlWebSSOMessageReceiver(
      SAML2ResponseValidator validator) {
    super(validator);
  }

  /**
   * Code is mostly copied from SAML2WebSSOMessageReceiver but instantiates a
   * HiveSamlHTTPPostDecoder instead.
   * @param webContext
   * @return
   */
  @Override
  public final AbstractPac4jDecoder getDecoder(final WebContext webContext) {
    final Pac4jHTTPPostDecoder decoder = new HiveSamlHTTPPostDecoder(webContext);
    try {
      decoder.setParserPool(Configuration.getParserPool());
      decoder.initialize();
      decoder.decode();
    } catch (final Exception e) {
      throw new SAMLException("Error decoding SAML message", e);
    }
    return decoder;
  }
}
