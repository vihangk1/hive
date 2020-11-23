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

import java.util.Optional;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.util.generator.ValueGenerator;

/**
 * Relay state generator for the SAML Request which includes the port number from the
 * request header. This port number is used eventually to redirect the token to the
 * localhost:port from the browser.
 */
public class HiveSamlRelayStateGenerator implements ValueGenerator {
  @Override
  public String generateValue(WebContext webContext) {
    Optional<String> portNumber = webContext
        .getRequestHeader(HiveSamlUtils.HIVE_SAML_RESPONSE_PORT);
    if (!portNumber.isPresent()) {
      throw new RuntimeException(
          "SAML response port header " + HiveSamlUtils.HIVE_SAML_RESPONSE_PORT
              + " is not set ");
    }
    return portNumber.get();
  }
}
