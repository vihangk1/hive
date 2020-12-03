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

public class HiveSamlRequestStateInfo {

  public static final String NO_VALID_GROUPS_ERR_MSG = "No valid groups were found for the user.";
  public static final String NO_VALID_SAML_RESPONSE = "SAML response could not be validated.";
  public static final String SUCCESS_MSG = "SAML response successfully validated.";
  public enum HiveSamlRequestState {
    PENDING, // SAML auth is pending
    SUCCESS, // SAML auth is successful, but client has not consumed it yet
    ERROR, // SAML auth failed
    CONSUMED // SAML auth was completed and client has been intimated of the result; we
    // are waiting to reap this RequestStateInfo. We should not use it to grant access.
  }
  private final HiveSamlRequestState requestState;
  private final String msg;
  private final String authenticatedUser;

  HiveSamlRequestStateInfo(
      HiveSamlRequestState requestState, String msg, String authenticatedUser) {
    this.requestState = requestState;
    this.msg = msg;
    this.authenticatedUser = authenticatedUser;
  }

  public HiveSamlRequestState getState() {
    return requestState;
  }

  public String getMessage() {
    return msg;
  }

  public String getAuthenticatedUser() {
    return authenticatedUser;
  }
}
