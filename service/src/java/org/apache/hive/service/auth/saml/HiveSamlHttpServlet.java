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

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hive.service.auth.saml.HiveSamlRequestStateInfo.HiveSamlRequestState;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HiveSamlHttpServlet extends HttpServlet {

  private static final Logger LOG = LoggerFactory
      .getLogger(HiveSamlHttpServlet.class);
  private final HiveConf conf;
  private final AuthTokenGenerator tokenGenerator;

  public HiveSamlHttpServlet(HiveConf conf) {
    this.conf = Preconditions.checkNotNull(conf);
    tokenGenerator = HiveSamlAuthTokenGenerator.get(conf);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    String nameId;
    String relayState;
    long clientIdentifier;
    try {
      relayState = HiveSamlRelayStateStore.get().getRelayStateInfo(request, response);
      clientIdentifier = HiveSamlRelayStateStore.get().getRelayStateInfo(relayState)
          .getClientIdentifier();
    } catch (HttpSamlAuthenticationException e) {
      LOG.error("Invalid relay state", e);
      response.setStatus(HttpStatus.SC_UNAUTHORIZED);
      return;
    }
    HiveSamlRequestStateInfo requestStateInfo = null;
    try {
      LOG.debug("Client identifier is " + clientIdentifier);
      nameId = HiveSaml2Client.get(conf).validate(request, response);
      Preconditions.checkState(nameId != null);
      requestStateInfo = new HiveSamlRequestStateInfo(HiveSamlRequestState.SUCCESS,
          HiveSamlRequestStateInfo.SUCCESS_MSG, nameId);
      sendBrowserMsg(response, true);
    } catch (HttpSamlAuthenticationException e) {
      if (e instanceof HttpSamlNoGroupsMatchedException) {
        LOG.error("Could not authenticate user since the groups didn't match", e);
        requestStateInfo = new HiveSamlRequestStateInfo(HiveSamlRequestState.ERROR,
            HiveSamlRequestStateInfo.NO_VALID_GROUPS_ERR_MSG, null);
      } else {
        LOG.error("SAML response could not be validated", e);
        requestStateInfo = new HiveSamlRequestStateInfo(HiveSamlRequestState.ERROR,
            HiveSamlRequestStateInfo.NO_VALID_SAML_RESPONSE, null);
      }
      sendBrowserMsg(response, false);
    } catch (Exception e) {
      LOG.error("Unexpected error received while processing the SAML response for client "
          + clientIdentifier, e);
      response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    } finally {
      if (requestStateInfo != null) {
        HiveSamlRelayStateStore.get().setRequestState(clientIdentifier, requestStateInfo);
      }
    }
  }

  private void sendBrowserMsg(HttpServletResponse response, boolean success) {
    StringBuilder sb = new StringBuilder();
    sb.append("<html>");
    sb.append("<title>Authentication Response Received</title>");
    sb.append("<body>");
    if (success) {
      sb.append("Successfully authenticated. You may close this window.");
    } else {
      sb.append(
          "Authentication failed. Please check server logs for details."
              + " You may close this window.");
    }
    sb.append("</body>");
    sb.append("</html>");
    try (PrintWriter writer = response.getWriter()) {
      writer.print(sb.toString());
    } catch (IOException e) {
      LOG.error("Error while generating the response.", e);
      response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
  }
}
