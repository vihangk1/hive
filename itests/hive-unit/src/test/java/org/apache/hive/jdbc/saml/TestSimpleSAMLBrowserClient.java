/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hive.jdbc.saml;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.util.Iterator;
import org.apache.hive.jdbc.Utils.JdbcConnectionParams;

/**
 * HTMLUnit based {@link IJdbcBrowserClient} for testing purposes.
 */
public class TestSimpleSAMLBrowserClient extends HiveJdbcBrowserClient {

  private final String username;
  private final String password;

  public TestSimpleSAMLBrowserClient(JdbcConnectionParams connectionParams,
      String username, String password)
      throws HiveJdbcBrowserException {
    super(connectionParams);
    this.username = username;
    this.password = password;
  }

  @Override
  protected void openBrowserWindow() throws HiveJdbcBrowserException {
    // if user and password are null, we fallback to real browser for interactive mode
    if (username == null && password == null) {
      super.doBrowserSSO();
      return;
    }
    try (WebClient webClient = new WebClient()) {
      final HtmlPage page = webClient.getPage(String.valueOf(clientContext.getSsoUri()));
      final HtmlForm form = page.getFormByName("f");
      final HtmlInput usernameInput = form.getInputByName("username");
      final HtmlInput passwordInput = form.getInputByName("password");
      usernameInput.setValueAttribute(username);
      passwordInput.setValueAttribute(password);
      for (DomElement element : page.getElementsByTagName("button")) {
        if ("button".equals(element.getTagName())) {
          element.click();
        }
      }
    } catch (IOException e) {
      throw new HiveJdbcBrowserException(e);
    }
  }
}