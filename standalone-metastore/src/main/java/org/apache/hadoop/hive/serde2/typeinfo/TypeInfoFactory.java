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

package org.apache.hadoop.hive.serde2.typeinfo;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.hadoop.hive.metastore.ColumnType;
import org.apache.hadoop.hive.metastore.type.MetastoreTypeInfoUtils;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveTypeEntry;

/**
 * TypeInfoFactory can be used to create the TypeInfo object for any types.
 *
 * TypeInfo objects are all read-only so we can reuse them easily.
 * TypeInfoFactory has internal cache to make sure we don't create 2 TypeInfo
 * objects that represents the same type.
 */
public final class TypeInfoFactory {

  private TypeInfoFactory() {
    // prevent instantiation
  }

  // Map from type name (such as int or varchar(40) to the corresponding PrimitiveTypeInfo
  // instance.
  private static ConcurrentHashMap<String, PrimitiveTypeInfo> cachedPrimitiveTypeInfo =
      new ConcurrentHashMap<String, PrimitiveTypeInfo>();

  /**
   * Register a Primitive Type. This registers primitiveTypeInfo to the TypeInfoFactory which
   * is cached and re-used whenever getPrimitiveTypeInfo is called with the same typename
   * @param typeName The typename for the TypeInfo
   * @param primitiveTypeInfo The primitiveTypeInfo object to be cached
   */
  public static void registerPrimitiveTypeInfo(final String typeName,
      final PrimitiveTypeInfo primitiveTypeInfo) {
    cachedPrimitiveTypeInfo.putIfAbsent(typeName, primitiveTypeInfo);
  }
  /**
   * Get PrimitiveTypeInfo instance for the given type name of a type
   * including types with parameters, such as varchar(20).
   *
   * @param typeName type name possibly with parameters.
   * @return aPrimitiveTypeInfo instance
   */
  public static PrimitiveTypeInfo getPrimitiveTypeInfo(String typeName) {
    PrimitiveTypeInfo result = cachedPrimitiveTypeInfo.get(typeName);
    if (result != null) {
      return result;
    }

    // Not found in the cache. Must be parameterized types. Create it.
    result = createPrimitiveTypeInfo(typeName);
    if (result == null) {
      throw new RuntimeException("Error creating PrimitiveTypeInfo instance for " + typeName);
    }

    PrimitiveTypeInfo prev = cachedPrimitiveTypeInfo.putIfAbsent(typeName, result);
    if (prev != null) {
      result = prev;
    }
    return result;
  }

  /**
   * Create PrimitiveTypeInfo instance for the given full name of the type. The returned
   * type is one of the parameterized type info such as VarcharTypeInfo.
   *
   * @param fullName Fully qualified name of the type
   * @return PrimitiveTypeInfo instance
   */
  private static PrimitiveTypeInfo createPrimitiveTypeInfo(String fullName) {
    String baseName = MetastoreTypeInfoUtils.getBaseName(fullName);
    PrimitiveTypeEntry typeEntry =
        PrimitiveTypeInfo.getPrimitiveTypeEntryFromTypeName(baseName);
    if (null == typeEntry) {
      throw new RuntimeException("Unknown type " + fullName);
    }

    MetastoreTypeInfoUtils.PrimitiveParts parts = MetastoreTypeInfoUtils.parsePrimitiveParts(fullName);
    if (parts.typeParams == null || parts.typeParams.length < 1) {
      return null;
    }

    switch (typeEntry.primitiveCategory) {
      case CHAR:
        if (parts.typeParams.length != 1) {
          return null;
        }
        return new CharTypeInfo(Integer.valueOf(parts.typeParams[0]));
      case VARCHAR:
        if (parts.typeParams.length != 1) {
          return null;
        }
        return new VarcharTypeInfo(Integer.valueOf(parts.typeParams[0]));
      case DECIMAL:
        if (parts.typeParams.length != 2) {
          return null;
        }
        return new DecimalTypeInfo(Integer.valueOf(parts.typeParams[0]),
            Integer.valueOf(parts.typeParams[1]));
      case TIMESTAMPLOCALTZ:
        if (parts.typeParams.length != 1) {
          return null;
        }
        return new TimestampLocalTZTypeInfo(parts.typeParams[0]);
      default:
        return null;
    }
  }

  public static CharTypeInfo getCharTypeInfo(int length) {
    String fullName = BaseCharTypeInfo.getQualifiedName(ColumnType.CHAR_TYPE_NAME, length);
    return (CharTypeInfo) getPrimitiveTypeInfo(fullName);
  }

  public static VarcharTypeInfo getVarcharTypeInfo(int length) {
    String fullName = BaseCharTypeInfo.getQualifiedName(ColumnType.VARCHAR_TYPE_NAME, length);
    return (VarcharTypeInfo) getPrimitiveTypeInfo(fullName);
  }

  public static DecimalTypeInfo getDecimalTypeInfo(int precision, int scale) {
    String fullName = DecimalTypeInfo.getQualifiedName(precision, scale);
    return (DecimalTypeInfo) getPrimitiveTypeInfo(fullName);
  };

  public static TimestampLocalTZTypeInfo getTimestampTZTypeInfo(ZoneId defaultTimeZone) {
    String fullName = TimestampLocalTZTypeInfo.getQualifiedName(defaultTimeZone);
    return (TimestampLocalTZTypeInfo) getPrimitiveTypeInfo(fullName);
  };

  static ConcurrentHashMap<ArrayList<List<?>>, TypeInfo> cachedStructTypeInfo =
    new ConcurrentHashMap<ArrayList<List<?>>, TypeInfo>();

  public static TypeInfo getStructTypeInfo(List<String> names,
      List<TypeInfo> typeInfos) {
    ArrayList<List<?>> signature = new ArrayList<List<?>>(2);
    signature.add(names);
    signature.add(typeInfos);
    TypeInfo result = cachedStructTypeInfo.get(signature);
    if (result == null) {
      result = new StructTypeInfo(names, typeInfos);
      TypeInfo prev = cachedStructTypeInfo.putIfAbsent(signature, result);
      if (prev != null) {
        result = prev;
      }
    }
    return result;
  }

  static ConcurrentHashMap<List<?>, TypeInfo> cachedUnionTypeInfo =
    new ConcurrentHashMap<List<?>, TypeInfo>();

  public static TypeInfo getUnionTypeInfo(List<TypeInfo> typeInfos) {
    TypeInfo result = cachedUnionTypeInfo.get(typeInfos);
    if (result == null) {
      result = new UnionTypeInfo(typeInfos);
      TypeInfo prev = cachedUnionTypeInfo.putIfAbsent(typeInfos, result);
      if (prev != null) {
        result = prev;
      }
    }
    return result;
  }

  static ConcurrentHashMap<TypeInfo, TypeInfo> cachedListTypeInfo = new ConcurrentHashMap<TypeInfo, TypeInfo>();

  public static TypeInfo getListTypeInfo(TypeInfo elementTypeInfo) {
    TypeInfo result = cachedListTypeInfo.get(elementTypeInfo);
    if (result == null) {
      result = new ListTypeInfo(elementTypeInfo);
      TypeInfo prev = cachedListTypeInfo.putIfAbsent(elementTypeInfo, result);
      if (prev != null) {
        result = prev;
      }
    }
    return result;
  }

  static ConcurrentHashMap<ArrayList<TypeInfo>, TypeInfo> cachedMapTypeInfo =
    new ConcurrentHashMap<ArrayList<TypeInfo>, TypeInfo>();

  public static TypeInfo getMapTypeInfo(TypeInfo keyTypeInfo,
      TypeInfo valueTypeInfo) {
    ArrayList<TypeInfo> signature = new ArrayList<TypeInfo>(2);
    signature.add(keyTypeInfo);
    signature.add(valueTypeInfo);
    TypeInfo result = cachedMapTypeInfo.get(signature);
    if (result == null) {
      result = new MapTypeInfo(keyTypeInfo, valueTypeInfo);
      TypeInfo prev = cachedMapTypeInfo.putIfAbsent(signature, result);
      if (prev != null) {
        result = prev;
      }
    }
    return result;
  }

}
