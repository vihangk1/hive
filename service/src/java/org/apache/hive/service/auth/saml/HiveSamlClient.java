package org.apache.hive.service.auth.saml;

import static org.opensaml.saml.common.xml.SAMLConstants.SAML2_POST_BINDING_URI;

import org.apache.hadoop.conf.Configuration;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.pac4j.saml.client.SAML2Client;
import org.pac4j.saml.config.SAML2Configuration;
import org.pac4j.saml.sso.impl.SAML2AuthnRequestBuilder;
import org.springframework.core.io.ClassPathResource;

public class HiveSamlClient {
  // TODO make this a singleton
  private static HiveSamlClient samlClient;
  private final SAML2Configuration saml2Configuration;

  public static synchronized HiveSamlClient get(Configuration conf) {
    if (samlClient != null) return samlClient;
    samlClient = new HiveSamlClient(conf);
    return samlClient;
  }

  private HiveSamlClient(Configuration conf) {
    // TODO pass these via hive-site.xml
    saml2Configuration = new SAML2Configuration(new ClassPathResource("samlKeystore.jks"),
        "pac4j-demo-passwd",
        "pac4j-demo-passwd",
        new ClassPathResource("idp-metadata.xml"));
    saml2Configuration.setAuthnRequestBindingType(SAMLConstants.SAML2_REDIRECT_BINDING_URI);
    saml2Configuration.setResponseBindingType(SAML2_POST_BINDING_URI);
    saml2Configuration.setCallbackUrl("http://localhost:10001/cliservice");
    saml2Configuration.setWantsAssertionsSigned(true);
    saml2Configuration.setAuthnRequestSigned(true);

    //TODO handle the replayCache as described in http://www.pac4j.org/docs/clients/saml.html
  }
}