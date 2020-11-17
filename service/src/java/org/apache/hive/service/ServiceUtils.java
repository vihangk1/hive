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
package org.apache.hive.service;

import java.io.IOException;

import org.apache.hadoop.hive.conf.HiveConf;
import org.slf4j.Logger;

public class ServiceUtils {

  /*
   * Get the index separating the user name from domain name (the user's name up
   * to the first '/' or '@').
   *
   * @param userName full user name.
   * @return index of domain match or -1 if not found
   */
  public static int indexOfDomainMatch(String userName) {
    if (userName == null) {
      return -1;
    }

    int idx = userName.indexOf('/');
    int idx2 = userName.indexOf('@');
    int endIdx = Math.min(idx, idx2); // Use the earlier match.
    // Unless at least one of '/' or '@' was not found, in
    // which case, user the latter match.
    if (endIdx == -1) {
      endIdx = Math.max(idx, idx2);
    }
    return endIdx;
  }

  /**
   * Close the Closeable objects and <b>ignore</b> any {@link IOException} or
   * null pointers. Must only be used for cleanup in exception handlers.
   *
   * @param log the log to record problems to at debug level. Can be null.
   * @param closeables the objects to close
   */
  public static void cleanup(Logger log, java.io.Closeable... closeables) {
    for (java.io.Closeable c : closeables) {
      if (c != null) {
        try {
          c.close();
        } catch(IOException e) {
          if (log != null && log.isDebugEnabled()) {
            log.debug("Exception in closing " + c, e);
          }
        }
      }
    }
  }

  public static boolean canProvideProgressLog(HiveConf hiveConf) {
    return ("tez".equals(hiveConf.getVar(HiveConf.ConfVars.HIVE_EXECUTION_ENGINE)) || "spark"
        .equals(hiveConf.getVar(HiveConf.ConfVars.HIVE_EXECUTION_ENGINE))) && hiveConf
        .getBoolVar(HiveConf.ConfVars.HIVE_SERVER2_INPLACE_PROGRESS);
  }

  /**
   * The config parameter can be like "path", "/path", "/path/", "path/*", "/path1/path2/*" and so on.
   * httpPath should end up as "/*", "/path/*" or "/path1/../pathN/*"
   * @param httpPath
   * @return
   */
  public static String getHttpPath(String httpPath) {
    if(httpPath == null || httpPath.equals("")) {
      httpPath = "/*";
    }
    else {
      if(!httpPath.startsWith("/")) {
        httpPath = "/" + httpPath;
      }
      if(httpPath.endsWith("/")) {
        httpPath = httpPath + "*";
      }
      if(!httpPath.endsWith("/*")) {
        httpPath = httpPath + "/*";
      }
    }
    return httpPath;
  }
}