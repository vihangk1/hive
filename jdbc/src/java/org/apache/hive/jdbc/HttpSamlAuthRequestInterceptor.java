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

package org.apache.hive.jdbc;

import com.google.common.base.Preconditions;
import java.util.Map;
import org.apache.hive.jdbc.saml.HiveJdbcBrowserClient;
import org.apache.hive.service.auth.saml.HiveSamlUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.client.CookieStore;
import org.apache.http.protocol.HttpContext;

public class HttpSamlAuthRequestInterceptor extends HttpRequestInterceptorBase {

  private final HiveJdbcBrowserClient browserClient;
  private static final String BEARER = "Bearer ";

  public HttpSamlAuthRequestInterceptor(HiveJdbcBrowserClient browserClient, CookieStore cookieStore, String cn,
      boolean isSSL, Map<String, String> additionalHeaders,
      Map<String, String> customCookies) {
    super(cookieStore, cn, isSSL, additionalHeaders, customCookies);
    this.browserClient = Preconditions.checkNotNull(browserClient);
  }

  @Override
  protected void addHttpAuthHeader(HttpRequest httpRequest, HttpContext httpContext)
      throws Exception {
    String port = String.valueOf(browserClient.getPort());
    String token = browserClient.getToken();
    if (token != null) {
      httpRequest.addHeader(HttpHeaders.AUTHORIZATION, BEARER + token);
      httpRequest.removeHeaders(HiveSamlUtils.HIVE_SAML_RESPONSE_PORT);
    } else {
      httpRequest.addHeader(HiveSamlUtils.HIVE_SAML_RESPONSE_PORT, port);
    }
  }
}
