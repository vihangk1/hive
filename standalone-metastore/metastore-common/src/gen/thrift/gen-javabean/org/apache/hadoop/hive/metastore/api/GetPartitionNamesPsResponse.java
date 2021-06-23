/**
 * Autogenerated by Thrift Compiler (0.14.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.apache.hadoop.hive.metastore.api;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.14.1)")
@org.apache.hadoop.classification.InterfaceAudience.Public @org.apache.hadoop.classification.InterfaceStability.Stable public class GetPartitionNamesPsResponse implements org.apache.thrift.TBase<GetPartitionNamesPsResponse, GetPartitionNamesPsResponse._Fields>, java.io.Serializable, Cloneable, Comparable<GetPartitionNamesPsResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("GetPartitionNamesPsResponse");

  private static final org.apache.thrift.protocol.TField NAMES_FIELD_DESC = new org.apache.thrift.protocol.TField("names", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new GetPartitionNamesPsResponseStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new GetPartitionNamesPsResponseTupleSchemeFactory();

  private @org.apache.thrift.annotation.Nullable java.util.List<java.lang.String> names; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    NAMES((short)1, "names");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // NAMES
          return NAMES;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.NAMES, new org.apache.thrift.meta_data.FieldMetaData("names", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(GetPartitionNamesPsResponse.class, metaDataMap);
  }

  public GetPartitionNamesPsResponse() {
  }

  public GetPartitionNamesPsResponse(
    java.util.List<java.lang.String> names)
  {
    this();
    this.names = names;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public GetPartitionNamesPsResponse(GetPartitionNamesPsResponse other) {
    if (other.isSetNames()) {
      java.util.List<java.lang.String> __this__names = new java.util.ArrayList<java.lang.String>(other.names);
      this.names = __this__names;
    }
  }

  public GetPartitionNamesPsResponse deepCopy() {
    return new GetPartitionNamesPsResponse(this);
  }

  @Override
  public void clear() {
    this.names = null;
  }

  public int getNamesSize() {
    return (this.names == null) ? 0 : this.names.size();
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Iterator<java.lang.String> getNamesIterator() {
    return (this.names == null) ? null : this.names.iterator();
  }

  public void addToNames(java.lang.String elem) {
    if (this.names == null) {
      this.names = new java.util.ArrayList<java.lang.String>();
    }
    this.names.add(elem);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.List<java.lang.String> getNames() {
    return this.names;
  }

  public void setNames(@org.apache.thrift.annotation.Nullable java.util.List<java.lang.String> names) {
    this.names = names;
  }

  public void unsetNames() {
    this.names = null;
  }

  /** Returns true if field names is set (has been assigned a value) and false otherwise */
  public boolean isSetNames() {
    return this.names != null;
  }

  public void setNamesIsSet(boolean value) {
    if (!value) {
      this.names = null;
    }
  }

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case NAMES:
      if (value == null) {
        unsetNames();
      } else {
        setNames((java.util.List<java.lang.String>)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case NAMES:
      return getNames();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case NAMES:
      return isSetNames();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof GetPartitionNamesPsResponse)
      return this.equals((GetPartitionNamesPsResponse)that);
    return false;
  }

  public boolean equals(GetPartitionNamesPsResponse that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_names = true && this.isSetNames();
    boolean that_present_names = true && that.isSetNames();
    if (this_present_names || that_present_names) {
      if (!(this_present_names && that_present_names))
        return false;
      if (!this.names.equals(that.names))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetNames()) ? 131071 : 524287);
    if (isSetNames())
      hashCode = hashCode * 8191 + names.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(GetPartitionNamesPsResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetNames(), other.isSetNames());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNames()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.names, other.names);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  @org.apache.thrift.annotation.Nullable
  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("GetPartitionNamesPsResponse(");
    boolean first = true;

    sb.append("names:");
    if (this.names == null) {
      sb.append("null");
    } else {
      sb.append(this.names);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (!isSetNames()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'names' is unset! Struct:" + toString());
    }

    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class GetPartitionNamesPsResponseStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public GetPartitionNamesPsResponseStandardScheme getScheme() {
      return new GetPartitionNamesPsResponseStandardScheme();
    }
  }

  private static class GetPartitionNamesPsResponseStandardScheme extends org.apache.thrift.scheme.StandardScheme<GetPartitionNamesPsResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, GetPartitionNamesPsResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // NAMES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list1358 = iprot.readListBegin();
                struct.names = new java.util.ArrayList<java.lang.String>(_list1358.size);
                @org.apache.thrift.annotation.Nullable java.lang.String _elem1359;
                for (int _i1360 = 0; _i1360 < _list1358.size; ++_i1360)
                {
                  _elem1359 = iprot.readString();
                  struct.names.add(_elem1359);
                }
                iprot.readListEnd();
              }
              struct.setNamesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, GetPartitionNamesPsResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.names != null) {
        oprot.writeFieldBegin(NAMES_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.names.size()));
          for (java.lang.String _iter1361 : struct.names)
          {
            oprot.writeString(_iter1361);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class GetPartitionNamesPsResponseTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public GetPartitionNamesPsResponseTupleScheme getScheme() {
      return new GetPartitionNamesPsResponseTupleScheme();
    }
  }

  private static class GetPartitionNamesPsResponseTupleScheme extends org.apache.thrift.scheme.TupleScheme<GetPartitionNamesPsResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, GetPartitionNamesPsResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        oprot.writeI32(struct.names.size());
        for (java.lang.String _iter1362 : struct.names)
        {
          oprot.writeString(_iter1362);
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, GetPartitionNamesPsResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      {
        org.apache.thrift.protocol.TList _list1363 = iprot.readListBegin(org.apache.thrift.protocol.TType.STRING);
        struct.names = new java.util.ArrayList<java.lang.String>(_list1363.size);
        @org.apache.thrift.annotation.Nullable java.lang.String _elem1364;
        for (int _i1365 = 0; _i1365 < _list1363.size; ++_i1365)
        {
          _elem1364 = iprot.readString();
          struct.names.add(_elem1364);
        }
      }
      struct.setNamesIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

