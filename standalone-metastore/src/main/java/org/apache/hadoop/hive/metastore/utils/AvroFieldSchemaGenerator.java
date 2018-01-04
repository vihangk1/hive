package org.apache.hadoop.hive.metastore.utils;

import org.apache.avro.Schema;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.serde2.avro.AvroSerdeException;
import org.apache.hadoop.hive.serde2.avro.SchemaToTypeInfo;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;

import java.util.ArrayList;
import java.util.List;

public class AvroFieldSchemaGenerator {
  final private List<String> columnNames;
  final private List<TypeInfo> columnTypes;
  final private List<String> columnComments;

  public AvroFieldSchemaGenerator(Schema schema) throws AvroSerdeException {
    verifySchemaIsARecord(schema);

    this.columnNames = generateColumnNames(schema);
    this.columnTypes = SchemaToTypeInfo.generateColumnTypes(schema);
    this.columnComments = generateColumnComments(schema);
    assert columnNames.size() == columnTypes.size();
  }

  private static void verifySchemaIsARecord(Schema schema) throws AvroSerdeException {
    if(!schema.getType().equals(Schema.Type.RECORD)) {
      throw new AvroSerdeException("Schema for table must be of type RECORD. " +
          "Received type: " + schema.getType());
    }
  }

  private static List<String> generateColumnNames(Schema schema) {
    List<Schema.Field> fields = schema.getFields();
    List<String> fieldsList = new ArrayList<String>(fields.size());

    for (Schema.Field field : fields) {
      fieldsList.add(field.name());
    }

    return fieldsList;
  }

  private static List<String> generateColumnComments(Schema schema) {
    List<Schema.Field> fields = schema.getFields();
    List<String> fieldComments = new ArrayList<String>(fields.size());

    for (Schema.Field field : fields) {
      String fieldComment = field.doc() == null ? "" : field.doc();
      fieldComments.add(fieldComment);
    }

    return fieldComments;
  }

  public List<FieldSchema> getFieldSchemas() throws AvroSerdeException {
    int len = columnNames.size();
    List<FieldSchema> fieldSchemas = new ArrayList<>(len);
    for(int i = 0; i<len; i++) {
      FieldSchema fieldSchema = new FieldSchema();
      fieldSchema.setName(columnNames.get(i));
      TypeInfo columnType = columnTypes.get(i);
      if(!supportedCategories(columnType)) {
        throw new AvroSerdeException("Don't yet support this type: " + columnType);
      }
      //In case of complex types getTypeName() will recusively go into typeName
      //of individual fields when the ColumnType was constructed
      //in SchemaToTypeInfo.generateColumnTypes in the constructor
      fieldSchema.setType(columnTypes.get(i).getTypeName());
      fieldSchema.setComment(StorageSchemaUtils.determineFieldComment(columnComments.get(i)));
    }
    return fieldSchemas;
  }

  private boolean supportedCategories(TypeInfo ti) {
    final ObjectInspector.Category c = ti.getCategory();
    return c.equals(ObjectInspector.Category.PRIMITIVE) ||
        c.equals(ObjectInspector.Category.MAP)       ||
        c.equals(ObjectInspector.Category.LIST)      ||
        c.equals(ObjectInspector.Category.STRUCT)    ||
        c.equals(ObjectInspector.Category.UNION);
  }
}
