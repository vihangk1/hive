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

package org.apache.hive.jdbc.saml;

import com.google.common.base.Preconditions;
import java.awt.Desktop;
import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.hive.service.auth.saml.HiveSamlUtils;

public class HiveJdbcBrowserClient implements Closeable {
  private URI ssoUri;
  private HiveSamlResponse serverResponse;
  private String codeChallenge;

  public static HiveJdbcBrowserClient create(Map<String, String> sessionConf)
      throws IOException {
    return new HiveJdbcBrowserClient(sessionConf);
  }

  private HiveJdbcBrowserClient(Map<String, String> sessionConf)
      throws IOException {
  }

  @Override
  public void close() throws IOException {
  }

  public void init(URI ssoUri, String codeChallenge) {
    // everytime we set the sso URI we should clean up the previous state if its set.
    // this may be from the previous invalid connection attempt or if the token has
    // expired
    reset();
    this.ssoUri = ssoUri;
    this.codeChallenge = codeChallenge;
  }

  private void reset() {
    if (serverResponse != null) {
      serverResponse = null;
    }
    codeChallenge = null;
  }

  private boolean validateSSOUrl(URI ssoUrl) {
    //TODO(Vihang) add URL validation code here
    return true;
  }

  public void doBrowserSSO() throws IOException {
    Preconditions.checkNotNull(ssoUri, "SSO Url is null");
    Preconditions.checkArgument(validateSSOUrl(ssoUri), "Invalid SSO url");
    if (Desktop.isDesktopSupported()) {
      Desktop.getDesktop().browse(ssoUri);
    } else {
      //Desktop is not supported, lets try to open the browser process
      OsType os = getOperatingSystem();
      switch (os) {
        case WINDOWS:
          //TODO(Vihang)
          break;
        case MAC:
          Runtime.getRuntime().exec("open " + ssoUri.toString());
          break;
        case LINUX:
          Runtime.getRuntime().exec("xdg-open " + ssoUri.toString());
          break;
        case UNKNOWN:
          throw new IOException(
              "Unknown operating system " + System.getProperty("os.name"));
      }
    }
  }

  public boolean getStatus() {
    return serverResponse != null && serverResponse.status;
  }

  public String getMessage() {
    return serverResponse == null ? "" : serverResponse.msg;
  }

  private static final class HiveSamlResponse {
    private final String msg;
    private final boolean status;
    private final String token;

    public HiveSamlResponse(String postResponse) {
      Map<String, String> params = parseUrlEncodedFormData(postResponse);
      status = Boolean.parseBoolean(params.get(HiveSamlUtils.STATUS_KEY));
      msg = params.getOrDefault(HiveSamlUtils.MESSAGE_KEY, "");
      token = params.get(HiveSamlUtils.TOKEN_KEY);
    }


    private Map<String, String> parseUrlEncodedFormData(String line) {
      String decoded;
      try {
        decoded = URLDecoder.decode(line, StandardCharsets.UTF_8.toString());
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      }
      Map<String, String> ret = new HashMap<>();
      for (String params : decoded.split("&")) {
        if (params.contains("=")) {
          String key = params.substring(0, params.indexOf("="));
          String val = params.substring(params.indexOf("=") + 1);
          ret.put(key, val);
        }
      }
      return ret;
    }
  }

  public String getToken() {
    return serverResponse == null ? null : serverResponse.token;
  }

  public String getClientIdentifier() {
    return codeChallenge;
  }

  public OsType getMatchingOs(String osName) {
    osName = osName.toLowerCase();
    if (osName.contains("win")) {
      return OsType.WINDOWS;
    }
    if (osName.contains("mac")) {
      return OsType.MAC;
    }
    if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
      return OsType.LINUX;
    }
    return OsType.UNKNOWN;
  }

  private enum OsType {
    WINDOWS,
    MAC,
    LINUX,
    UNKNOWN
  }

  private OsType getOperatingSystem() {
    String osName = System.getProperty("os.name");
    Preconditions.checkNotNull(osName, "os.name is null");
    return getMatchingOs(osName);
  }
}
