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

package org.apache.hadoop.hive.ql.exec.vector.expressions;

import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;

public class FilterConstantBooleanVectorExpression extends ConstantVectorExpression {

  private static final long serialVersionUID = 1L;

  public FilterConstantBooleanVectorExpression() {
    super();
  }

  public FilterConstantBooleanVectorExpression(long value) throws HiveException {
    super(-1, value, TypeInfoUtils.booleanTypeInfo);
  }

  @Override
  public void evaluate(VectorizedRowBatch vrg) {
    if (longValue == 0) {
      // All values are filtered out
      vrg.size = 0;
    }
  }
}
