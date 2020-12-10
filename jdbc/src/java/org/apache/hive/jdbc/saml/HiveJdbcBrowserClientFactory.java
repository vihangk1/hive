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

import java.util.Map;
import org.apache.hadoop.hive.conf.HiveConf.ConfVars;
import org.apache.hive.jdbc.saml.IJdbcBrowserClient.HiveJdbcBrowserException;

/**
 * A factory to instantiate {@link IJdbcBrowserClient} objects. This is currently mostly
 * used to make sure we can instantiate a test browser client which does the browser
 * flow programmatically to run automated tests.
 */
public class HiveJdbcBrowserClientFactory {

  /**
   * Returns an instance of {@link IJdbcBrowserClient} as per the configuration. If
   * hive.test.saml.browser.class is set on the HS2 server side, then this factory will
   * use its value to instantiate the given class name.
   *
   * @param hiveParams as received from the server side hive configuration.
   * @return An instance of {@link IJdbcBrowserClient}
   * @throws HiveJdbcBrowserException In case the test browser class could not be
   *                                  instantiated.
   */
  public static IJdbcBrowserClient create(Map<String, String> hiveParams)
      throws HiveJdbcBrowserException {
    String testClassName = hiveParams
        .get(ConfVars.HIVE_TEST_MODE_SAML_BROWSER_AUTH.varname);
    if (testClassName != null && !testClassName.isEmpty()) {
      String className = hiveParams.get(ConfVars.HIVE_TEST_MODE_SAML_BROWSER_AUTH.varname);
      try {
        return (IJdbcBrowserClient) Class.forName(className).newInstance();
      } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
        throw new HiveJdbcBrowserException(
            "Unable to instantiate browser client " + className);
      }
    }
    return new HiveJdbcBrowserClient(hiveParams);
  }
}
