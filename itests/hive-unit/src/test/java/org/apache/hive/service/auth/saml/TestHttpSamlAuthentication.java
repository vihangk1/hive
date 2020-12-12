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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.conf.HiveConf.ConfVars;
import org.apache.hive.jdbc.HiveConnection;
import org.apache.hive.jdbc.Utils.JdbcConnectionParams;
import org.apache.hive.jdbc.miniHS2.MiniHS2;
import org.apache.hive.jdbc.saml.IJdbcBrowserClient;
import org.apache.hive.jdbc.saml.IJdbcBrowserClient.HiveJdbcBrowserException;
import org.apache.hive.jdbc.saml.IJdbcBrowserClientFactory;
import org.apache.hive.jdbc.saml.TestSimpleSAMLBrowserClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class TestHttpSamlAuthentication {

  private static final String USER1 = "user1";
  private static final String USER1_PASSWORD = "user1pass";
  private static final String USER2 = "user2";
  private static final String USER2_PASSWORD = "user2pass";
  private static MiniHS2 miniHS2;
  private static final String USER_PASS_MODE = "example-userpass";

  public static GenericContainer genericContainer;
  private static final File tmpDir = Files.createTempDir();
  private static final File idpMetadataFile = new File(tmpDir, "idp-metadata.xml");

  @BeforeClass
  public static void setupHS2() throws Exception {
    HiveConf conf = new HiveConf();
    conf.setBoolVar(ConfVars.HIVE_SUPPORT_CONCURRENCY, false);
    conf.setBoolVar(ConfVars.HIVE_SERVER2_LOGGING_OPERATION_ENABLED, false);
    conf.setBoolVar(ConfVars.HIVESTATSCOLAUTOGATHER, false);
    conf.setVar(ConfVars.HIVE_SERVER2_AUTHENTICATION, "SAML2_0");
    conf.setVar(ConfVars.HIVE_SERVER2_SAML_IDP_METADATA,
        idpMetadataFile.getAbsolutePath());
    conf.setVar(ConfVars.HIVE_SERVER2_SAML_SP_ID, "test-hive-SAML-sp");
    conf.setVar(ConfVars.HIVE_SERVER2_SAML_KEYSTORE_PATH,
        new File(tmpDir, "saml_keystore.jks").getAbsolutePath());
    conf.setVar(ConfVars.HIVE_SERVER2_SAML_KEYSTORE_PASSWORD, "test-password");
    conf.setVar(ConfVars.HIVE_SERVER2_SAML_PRIVATE_KEY_PASSWORD, "secret");
    miniHS2 = new MiniHS2.Builder().withConf(conf).withHTTPTransport().build();
  }

  @AfterClass
  public static void stopServices() throws Exception {
    if (miniHS2 != null && miniHS2.isStarted()) {
      miniHS2.stop();
      miniHS2.cleanup();
      miniHS2 = null;
      MiniHS2.cleanupLocalDir();
    }
    if (tmpDir.exists()) {
      tmpDir.delete();
    }
    if (genericContainer != null && genericContainer.isRunning()) {
      genericContainer.stop();
    }
  }

  @After
  public void cleanUpIdpEnv() {
    if (genericContainer != null) {
      genericContainer.stop();
      genericContainer = null;
    }
    if (miniHS2 != null) {
      miniHS2.stop();
    }
  }

  private void setupIDP(boolean useSignedAssertions, String authMode) throws Exception {
    Map<String, String> configOverlay = new HashMap<>();
    configOverlay.put(ConfVars.HIVE_SERVER2_SAML_CALLBACK_URL.varname,
        "http://localhost:" + miniHS2.getHttpPort()
            + "/sso/saml?client_name=HiveSaml2Client");
    miniHS2.start(configOverlay);
    Map<String, String> idpEnv = getIdpEnv(useSignedAssertions, authMode);
    genericContainer = new GenericContainer<>(
        DockerImageName.parse("test-saml-idp:1.0"))
        .withExposedPorts(8080, 8443)
        .withEnv(idpEnv);
    genericContainer.start();
    Integer ssoPort = genericContainer.getMappedPort(8080);
    writeIdpMetadataFile(ssoPort, idpMetadataFile);
  }

  private static void writeIdpMetadataFile(Integer ssoPort, File targetFile)
      throws IOException {
    String metadata = Resources.toString(
        Resources.getResource("simple-saml-idp-metadata-template.xml"),
        Charsets.UTF_8);
    metadata = metadata.replace("REPLACE_ME", ssoPort.toString());
    Files.write(metadata, targetFile, StandardCharsets.UTF_8);
  }

  private static Map<String, String> getIdpEnv(boolean useSignedAssertions,
      String authMode) throws Exception {
    Map<String, String> idpEnv = new HashMap<>();
    idpEnv
        .put("SIMPLESAMLPHP_SP_ASSERTION_CONSUMER_SERVICE", HiveSamlUtils.getCallBackUri(
            miniHS2.getServerConf()).toString());
    idpEnv.put("SIMPLESAMLPHP_SP_ENTITY_ID",
        miniHS2.getHiveConf().get(ConfVars.HIVE_SERVER2_SAML_SP_ID.varname));
    idpEnv.put("SIMPLESAMLPHP_SP_NAME_ID_FORMAT",
        "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress");
    idpEnv.put("SIMPLESAMLPHP_SP_NAME_ID_ATTRIBUTE", "email");
    idpEnv.put("SIMPLESAMLPHP_IDP_AUTH", authMode);
    // by default idp signs the assertions
    if (!useSignedAssertions) {
      idpEnv.put("SIMPLESAMLPHP_SP_SIGN_ASSERTION", "false");
    }
    return idpEnv;
  }

  private static String getSamlJdbcConnectionUrl() throws Exception {
    return miniHS2.getHttpJdbcURL() + "auth=browser;browserResponseTimeout=0";
  }

  private static class TestHiveJdbcBrowserClientFactory implements
      IJdbcBrowserClientFactory {
    private String user;
    private String password;
    TestHiveJdbcBrowserClientFactory(String user, String password) {
      this.user = user;
      this.password = password;
    }

    @Override
    public IJdbcBrowserClient create(JdbcConnectionParams connectionParams)
        throws HiveJdbcBrowserException {
      return new TestSimpleSAMLBrowserClient(connectionParams, user
          , password);
    }
  }

  /**
   * Test HiveConnection which injects a HTMLUnit based browser client.
   */
  private static class TestHiveConnection extends HiveConnection {
    public TestHiveConnection(String uri, Properties info) throws SQLException {
      super(uri, info);
    }

    public TestHiveConnection(String uri, Properties info, String testUser,
        String testPass) throws SQLException {
      super(uri, info, new TestHiveJdbcBrowserClientFactory(testUser, testPass));
    }
  }

  @Test(expected = SQLException.class)
  public void testFailureForUnsignedResponse() throws Exception {
    setupIDP(false, USER_PASS_MODE);
    try (TestHiveConnection connection = new TestHiveConnection(
        getSamlJdbcConnectionUrl(), new Properties(), USER1, USER1_PASSWORD)) {
      fail("Expected a failure since SAML response is not signed");
    }
  }

  @Test
  public void testBasicConnection() throws Exception {
    setupIDP(true, USER_PASS_MODE);
    try (TestHiveConnection connection = new TestHiveConnection(
        getSamlJdbcConnectionUrl(), new Properties(), USER1, USER1_PASSWORD)) {
      assertLoggedInUser(connection, USER1);
    }
    try (TestHiveConnection connection = new TestHiveConnection(
        getSamlJdbcConnectionUrl(), new Properties(), USER2, USER2_PASSWORD)) {
      assertLoggedInUser(connection, USER2);
    }
  }

  private void assertLoggedInUser(HiveConnection connection, String expectedUser)
      throws SQLException {
    Statement stmt = connection.createStatement();
    ResultSet resultSet = stmt.executeQuery("select logged_in_user()");
    assertTrue(resultSet.next());
    String loggedInUser = resultSet.getString(1);
    assertEquals(expectedUser, loggedInUser);
  }
}
