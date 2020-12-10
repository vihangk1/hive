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

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.conf.HiveConf.ConfVars;
import org.apache.hive.jdbc.HiveConnection;
import org.apache.hive.jdbc.miniHS2.MiniHS2;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class TestHttpSamlAuthentication {

  private static MiniHS2 miniHS2;
  private static Map<String, String> idpEnv;

  public static GenericContainer genericContainer;
  private static final File tmpDir = Files.createTempDir();
  private static final File idpMetadataFile = new File(tmpDir, "idp-metadata.xml");

  @BeforeClass
  public static void startServices() throws Exception {
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
  }

  @Before
  public void setIdpEnv() throws Exception {
    Map<String, String> configOverlay = new HashMap<>();
    configOverlay.put(ConfVars.HIVE_SERVER2_SAML_CALLBACK_URL.varname,
        "http://localhost:" + miniHS2.getHttpPort()
            + "/sso/saml?client_name=HiveSaml2Client");
    miniHS2.start(configOverlay);
    idpEnv = getIdpEnv();
    genericContainer = new GenericContainer<>(
        DockerImageName.parse("test-saml-idp:1.0"))
        .withExposedPorts(8080, 8443)
        .withEnv(idpEnv);
    genericContainer.start();
    Integer ssoPort = genericContainer.getMappedPort(8080);
    writeIdpMetadataFile(ssoPort, idpMetadataFile);
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

  private static void writeIdpMetadataFile(Integer ssoPort, File targetFile)
      throws IOException {
    String metadata = Resources.toString(
        Resources.getResource("simple-saml-idp-metadata-template.xml"),
        Charsets.UTF_8);
    metadata = metadata.replace("REPLACE_ME", ssoPort.toString());
    Files.write(metadata, targetFile, StandardCharsets.UTF_8);
  }

  private static Map<String, String> getIdpEnv() throws Exception {
    Map<String, String> idpEnv = new HashMap<>();
    idpEnv
        .put("SIMPLESAMLPHP_SP_ASSERTION_CONSUMER_SERVICE", HiveSamlUtils.getCallBackUri(
            miniHS2.getServerConf()).toString());
    idpEnv.put("SIMPLESAMLPHP_SP_ENTITY_ID",
        miniHS2.getHiveConf().get(ConfVars.HIVE_SERVER2_SAML_SP_ID.varname));
    idpEnv.put("SIMPLESAMLPHP_SP_NAME_ID_FORMAT",
        "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress");
    idpEnv.put("SIMPLESAMLPHP_SP_NAME_ID_ATTRIBUTE", "email");
    return idpEnv;
  }

  private static String getSamlJdbcConnectionUrl() throws Exception {
    return miniHS2.getHttpJdbcURL() + "auth=browser";
  }

  @Test
  public void testBasicConnection() throws Exception {
    try (HiveConnection connection = (HiveConnection) DriverManager
        .getConnection(getSamlJdbcConnectionUrl())) {
      assertLoggedInUser(connection, "user1");
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
