package org.apache.hadoop.hive.metastore.type;

import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoParser;

public class MetastoreTypeInfoUtils {

  public static String getBaseName(String typeName) {
    int idx = typeName.indexOf('(');
    if (idx == -1) {
      return typeName;
    } else {
      return typeName.substring(0, idx);
    }
  }

  public static class PrimitiveParts {
    public String  typeName;
    public String[] typeParams;
  }

  /**
   * Make some of the TypeInfo parsing available as a utility.
   */
  public static PrimitiveParts parsePrimitiveParts(String typeInfoString) {
    TypeInfoParser parser = new TypeInfoParser(typeInfoString);
    return parser.parsePrimitiveParts();
  }
}
