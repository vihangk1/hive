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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HiveSamlHttpServlet extends HttpServlet {

  private static final Logger LOG = LoggerFactory
      .getLogger(HiveSamlHttpServlet.class);
  private final HiveConf conf;
  private static final ObjectMapper objMapper = new ObjectMapper();

  public HiveSamlHttpServlet(HiveConf conf) {
    this.conf = Preconditions.checkNotNull(conf);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    LOG.info("VIHANG-DEBUG: Received request");
    try {
      String credentials = HiveSaml2Client.get(conf).validate(request, response);
      LOG.info("VIHANG-DEBUG: received credentials as " + credentials);
      Map<String, String> keyValues = new HashMap<>();
      keyValues.put("status", "success");
      keyValues.put("token", "test-token");
      keyValues.put("message", "");
      String payload = objMapper.writeValueAsString(keyValues);
      try (PrintWriter writer = response.getWriter()) {
        writer.write(payload);
      }
      response.sendRedirect("http://localhost:9999");
    } catch (HttpSamlAuthenticationException e) {
      LOG.error("Invalid SAMl response received", e);
      response.setStatus(HttpStatus.SC_UNAUTHORIZED);
    }
  }
}
