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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.http.HttpStatus;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HiveSamlHttpServlet extends HttpServlet {

  private static final Logger LOG = LoggerFactory
      .getLogger(HiveSamlHttpServlet.class);
  private final HiveConf conf;
  private static final ObjectMapper objMapper = new ObjectMapper();
  private final AuthTokenGenerator tokenGenerator;

  public HiveSamlHttpServlet(HiveConf conf) {
    this.conf = Preconditions.checkNotNull(conf);
    tokenGenerator = SamlAuthTokenGenerator.get(conf);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    String nameId = null;
    Integer port = null;
    try {
      port = extractRelayState(request, response);
      LOG.debug("RelayState is " + port);
      nameId = HiveSaml2Client.get(conf).validate(request, response);
    } catch (HttpSamlAuthenticationException e) {
      LOG.error("Invalid SAML response received", e);
    }
    try {
      if (nameId != null) {
        LOG.debug(
            "Successfully validated saml response. Forwarding the token to port " + port);
        generateFormData(response, "http://localhost:" + port, tokenGenerator.get(nameId),
            true, "");
      } else {
        generateFormData(response, "http://localhost:" + port, null, false,
            "SAML assertion could not be validated. Check server logs for details.");
      }
    } catch (IOException e) {
      LOG.error("Could not process the SAML response", e);
      response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private Integer extractRelayState(HttpServletRequest request,
      HttpServletResponse response) throws HttpSamlAuthenticationException {
    String relayState = request.getParameter("RelayState");
    if (relayState == null) {
      throw new HttpSamlAuthenticationException("Could not get the RelayState from the SAML response");
    }
    try {
     return
         Integer.parseInt(relayState);
    } catch (NumberFormatException e) {
      throw new HttpSamlAuthenticationException("Invalid value of relay state received: " + relayState);
    }
  }

  private void generateFormData(HttpServletResponse response, String url, String token,
      boolean sucess, String msg) throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append("<html>");
    sb.append("<body onload='document.forms[\"form\"].submit()'>");
    sb.append(String.format("<form name='form' action='%s' method='POST'>", url));
    sb.append(String.format("<input type='hidden' name='token' value='%s'>", token));
    sb.append(String.format("<input type='hidden' name='success' value='%s'>",
        String.valueOf(sucess)));
    sb.append(String.format("<input type='hidden' name='message' value='%s'>", msg));
    sb.append("</form>");
    sb.append("</body>");
    sb.append("</html>");
    try (PrintWriter write = response.getWriter()) {
      write.write(sb.toString());
    }
  }
}
