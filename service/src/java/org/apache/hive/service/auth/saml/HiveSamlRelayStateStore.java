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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.hive.service.auth.saml.HiveSamlRequestStateInfo.HiveSamlRequestState;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.util.generator.ValueGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Relay state generator for the SAML Request which includes the port number from the
 * request header. This port number is used eventually to redirect the token to the
 * localhost:port from the browser.
 */
public class HiveSamlRelayStateStore implements ValueGenerator {

  private final Cache<String, HiveSamlRelayStateInfo> relayStateCache =
      CacheBuilder.newBuilder()
          //TODO(Vihang) make this configurable
          .expireAfterWrite(5, TimeUnit.MINUTES)
          .build();
  private static final Random randGenerator = new SecureRandom();
  private static final Logger LOG = LoggerFactory
      .getLogger(HiveSamlRelayStateStore.class);

  //TODO(Vihang) add time based eviction here.
  private final ConcurrentHashMap<Long, HiveSamlRequestStateInfo>
      requestStateInfoMap = new ConcurrentHashMap<>();
  private static final HiveSamlRelayStateStore INSTANCE = new HiveSamlRelayStateStore();
  private final Object requestStateNotifier = new Object();

  private HiveSamlRelayStateStore() {
  }

  public static HiveSamlRelayStateStore get() {
    return INSTANCE;
  }

  @Override
  public String generateValue(WebContext webContext) {
    String relayState = UUID.randomUUID().toString();
    HiveSamlRelayStateInfo relayStateInfo = new HiveSamlRelayStateInfo(
        randGenerator.nextLong());
    webContext.setResponseHeader(HiveSamlUtils.HIVE_SAML_CLIENT_IDENTIFIER,
        String.valueOf(relayStateInfo.getClientIdentifier()));
    relayStateCache.put(relayState, relayStateInfo);
    return relayState;
  }

  public String getRelayStateInfo(HttpServletRequest request,
      HttpServletResponse response)
      throws HttpSamlAuthenticationException {
    String relayState = request.getParameter("RelayState");
    if (relayState == null) {
      throw new HttpSamlAuthenticationException(
          "Could not get the RelayState from the SAML response");
    }
    return relayState;
  }

  public HiveSamlRelayStateInfo getRelayStateInfo(String relayState)
      throws HttpSamlAuthenticationException {
    HiveSamlRelayStateInfo relayStateInfo = relayStateCache.getIfPresent(relayState);
    if (relayStateInfo == null) {
      throw new HttpSamlAuthenticationException(
          "Invalid value of relay state received: " + relayState);
    }
    return relayStateInfo;
  }

  public void setRequestState(Long clientIdentifier,
      HiveSamlRequestStateInfo requestStateInfo) {
    synchronized (requestStateNotifier) {
    requestStateInfoMap.put(clientIdentifier, requestStateInfo);
      requestStateNotifier.notifyAll();
    }
  }

  public HiveSamlRequestStateInfo waitUntilRequestIsProcessed(final long clientIdentifier,
      long timeOutInMs) throws HttpSamlAuthenticationException {
    HiveSamlRequestStateInfo requestStateInfo;
    synchronized (requestStateNotifier) {
      requestStateInfo = requestStateInfoMap.get(clientIdentifier);
      while (requestStateInfo == null
          || requestStateInfo.getState() == HiveSamlRequestState.PENDING) {
        try {
          requestStateNotifier.wait(timeOutInMs);
        } catch (InterruptedException e) {
          // ignored
        }
        requestStateInfo = requestStateInfoMap.get(clientIdentifier);
      }
    }
    return requestStateInfo;
  }
}