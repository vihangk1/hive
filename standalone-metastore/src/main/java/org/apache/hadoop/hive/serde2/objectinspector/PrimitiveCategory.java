package org.apache.hadoop.hive.serde2.objectinspector;

/**
 * The primitive types supported by Hive.
 */
public enum PrimitiveCategory {
  VOID, BOOLEAN, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, STRING,
  DATE, TIMESTAMP, TIMESTAMPLOCALTZ, BINARY, DECIMAL, VARCHAR, CHAR,
  INTERVAL_YEAR_MONTH, INTERVAL_DAY_TIME, UNKNOWN
}
