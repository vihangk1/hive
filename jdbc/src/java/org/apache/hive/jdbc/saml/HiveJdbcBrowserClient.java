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
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HiveJdbcBrowserClient implements Closeable {

  private final Map<String, String> sessionConf;
  private final ServerSocket serverSocket;
  private static final Logger LOG = LoggerFactory.getLogger(HiveJdbcBrowserClient.class);
  private String origin;
  private URI ssoUri;
  private final Integer port;
  private String samlResponse;

  public static HiveJdbcBrowserClient create(Map<String, String> sessionConf)
      throws IOException {
    return new HiveJdbcBrowserClient(sessionConf);
  }

  private HiveJdbcBrowserClient(Map<String, String> sessionConf)
      throws IOException {
    this.sessionConf = new ImmutableMap.Builder<String, String>().putAll(sessionConf)
        .build();
    // TODO(Vihang) make this configurable.
    int port = Integer.parseInt(sessionConf.getOrDefault("saml.response.port", "0"));
    port = 9999;
    serverSocket = new ServerSocket(port, 0,
        InetAddress.getByName("localhost"));
    this.port = serverSocket.getLocalPort();
  }

  public Integer getPort() { return this.port; }

  @Override
  public void close() throws IOException {
    if (serverSocket != null) {
      serverSocket.close();
    }
    samlResponse = null;
  }

  public void setSsoUri(URI ssoUri) {
    this.ssoUri = ssoUri;
  }

  private boolean validateSSOUrl(URI ssoUrl) {
    //TODO(Vihang) add validation code here
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
    // listen to the response on the server socket
    while (true) {
      Socket socket = serverSocket.accept();
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(
          socket.getInputStream(), StandardCharsets.UTF_8))) {
        char[] buffer = new char[16 * 1024];
        // block until you read into the buffer
        int len = reader.read(buffer);
        String response = String.valueOf(buffer, 0, len);
        LOG.info("Received response : " + response);
        String[] lines = response.split("\r\n");
        for (String line : lines) {
          if (!Strings.isNullOrEmpty(line)) {
            if (line.startsWith("SAMLResponse=")) {
              samlResponse = line.substring("SAMLResponse=".length());
              sendSuccess(socket);
            }
          }
        }
        if (samlResponse == null) {
          throw new IOException("Did not receive SAML response");
        }
        break;
      }
    }
  }

  public String getSamlResponse() {
    return samlResponse;
  }

  private void sendSuccess(Socket socket) throws IOException {
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

    List<String> content = new ArrayList<>();
    content.add("HTTP/1.0 200 OK");
    content.add("Content-Type: text/html");
    String responseText =
        "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"/>"
            + "<title>SAML Response Received</title></head>"
            + "<body>SAML Response received. You may close this window.</body></html>";
    content.add(String.format("Content-Length: %s", responseText.length()));
    content.add("");
    content.add(responseText);

    for (int i = 0; i < content.size(); ++i) {
      if (i > 0) {
        out.print("\r\n");
      }
      out.print(content.get(i));
    }
    out.flush();
  }

  private void sendFail(Socket socket) throws IOException {
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

    List<String> content = new ArrayList<>();
    content.add("HTTP/1.0 200 OK");
    content.add("Content-Type: text/html");
    String responseText =
        "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"/>"
            + "<title>SAML Response Received</title></head>"
            + "<body>SAML Response received. You may close this window.</body></html>";
    content.add(String.format("Content-Length: %s", responseText.length()));
    content.add("");
    content.add(responseText);

    for (int i = 0; i < content.size(); ++i) {
      if (i > 0) {
        out.print("\r\n");
      }
      out.print(content.get(i));
    }
    out.flush();
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
