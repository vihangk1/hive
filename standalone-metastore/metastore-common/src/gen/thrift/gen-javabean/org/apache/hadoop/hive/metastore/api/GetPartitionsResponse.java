/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.apache.hadoop.hive.metastore.api;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)")
@org.apache.hadoop.classification.InterfaceAudience.Public @org.apache.hadoop.classification.InterfaceStability.Stable public class GetPartitionsResponse implements org.apache.thrift.TBase<GetPartitionsResponse, GetPartitionsResponse._Fields>, java.io.Serializable, Cloneable, Comparable<GetPartitionsResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("GetPartitionsResponse");

  private static final org.apache.thrift.protocol.TField PARTITION_SPEC_FIELD_DESC = new org.apache.thrift.protocol.TField("partitionSpec", org.apache.thrift.protocol.TType.LIST, (short)1);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new GetPartitionsResponseStandardSchemeFactory());
    schemes.put(TupleScheme.class, new GetPartitionsResponseTupleSchemeFactory());
  }

  private List<PartitionSpec> partitionSpec; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    PARTITION_SPEC((short)1, "partitionSpec");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // PARTITION_SPEC
          return PARTITION_SPEC;
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
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.PARTITION_SPEC, new org.apache.thrift.meta_data.FieldMetaData("partitionSpec", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, PartitionSpec.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(GetPartitionsResponse.class, metaDataMap);
  }

  public GetPartitionsResponse() {
  }

  public GetPartitionsResponse(
    List<PartitionSpec> partitionSpec)
  {
    this();
    this.partitionSpec = partitionSpec;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public GetPartitionsResponse(GetPartitionsResponse other) {
    if (other.isSetPartitionSpec()) {
      List<PartitionSpec> __this__partitionSpec = new ArrayList<PartitionSpec>(other.partitionSpec.size());
      for (PartitionSpec other_element : other.partitionSpec) {
        __this__partitionSpec.add(new PartitionSpec(other_element));
      }
      this.partitionSpec = __this__partitionSpec;
    }
  }

  public GetPartitionsResponse deepCopy() {
    return new GetPartitionsResponse(this);
  }

  @Override
  public void clear() {
    this.partitionSpec = null;
  }

  public int getPartitionSpecSize() {
    return (this.partitionSpec == null) ? 0 : this.partitionSpec.size();
  }

  public java.util.Iterator<PartitionSpec> getPartitionSpecIterator() {
    return (this.partitionSpec == null) ? null : this.partitionSpec.iterator();
  }

  public void addToPartitionSpec(PartitionSpec elem) {
    if (this.partitionSpec == null) {
      this.partitionSpec = new ArrayList<PartitionSpec>();
    }
    this.partitionSpec.add(elem);
  }

  public List<PartitionSpec> getPartitionSpec() {
    return this.partitionSpec;
  }

  public void setPartitionSpec(List<PartitionSpec> partitionSpec) {
    this.partitionSpec = partitionSpec;
  }

  public void unsetPartitionSpec() {
    this.partitionSpec = null;
  }

  /** Returns true if field partitionSpec is set (has been assigned a value) and false otherwise */
  public boolean isSetPartitionSpec() {
    return this.partitionSpec != null;
  }

  public void setPartitionSpecIsSet(boolean value) {
    if (!value) {
      this.partitionSpec = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case PARTITION_SPEC:
      if (value == null) {
        unsetPartitionSpec();
      } else {
        setPartitionSpec((List<PartitionSpec>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case PARTITION_SPEC:
      return getPartitionSpec();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case PARTITION_SPEC:
      return isSetPartitionSpec();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof GetPartitionsResponse)
      return this.equals((GetPartitionsResponse)that);
    return false;
  }

  public boolean equals(GetPartitionsResponse that) {
    if (that == null)
      return false;

    boolean this_present_partitionSpec = true && this.isSetPartitionSpec();
    boolean that_present_partitionSpec = true && that.isSetPartitionSpec();
    if (this_present_partitionSpec || that_present_partitionSpec) {
      if (!(this_present_partitionSpec && that_present_partitionSpec))
        return false;
      if (!this.partitionSpec.equals(that.partitionSpec))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_partitionSpec = true && (isSetPartitionSpec());
    list.add(present_partitionSpec);
    if (present_partitionSpec)
      list.add(partitionSpec);

    return list.hashCode();
  }

  @Override
  public int compareTo(GetPartitionsResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetPartitionSpec()).compareTo(other.isSetPartitionSpec());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPartitionSpec()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.partitionSpec, other.partitionSpec);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("GetPartitionsResponse(");
    boolean first = true;

    sb.append("partitionSpec:");
    if (this.partitionSpec == null) {
      sb.append("null");
    } else {
      sb.append(this.partitionSpec);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class GetPartitionsResponseStandardSchemeFactory implements SchemeFactory {
    public GetPartitionsResponseStandardScheme getScheme() {
      return new GetPartitionsResponseStandardScheme();
    }
  }

  private static class GetPartitionsResponseStandardScheme extends StandardScheme<GetPartitionsResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, GetPartitionsResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // PARTITION_SPEC
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list1192 = iprot.readListBegin();
                struct.partitionSpec = new ArrayList<PartitionSpec>(_list1192.size);
                PartitionSpec _elem1193;
                for (int _i1194 = 0; _i1194 < _list1192.size; ++_i1194)
                {
                  _elem1193 = new PartitionSpec();
                  _elem1193.read(iprot);
                  struct.partitionSpec.add(_elem1193);
                }
                iprot.readListEnd();
              }
              struct.setPartitionSpecIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, GetPartitionsResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.partitionSpec != null) {
        oprot.writeFieldBegin(PARTITION_SPEC_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.partitionSpec.size()));
          for (PartitionSpec _iter1195 : struct.partitionSpec)
          {
            _iter1195.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class GetPartitionsResponseTupleSchemeFactory implements SchemeFactory {
    public GetPartitionsResponseTupleScheme getScheme() {
      return new GetPartitionsResponseTupleScheme();
    }
  }

  private static class GetPartitionsResponseTupleScheme extends TupleScheme<GetPartitionsResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, GetPartitionsResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetPartitionSpec()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetPartitionSpec()) {
        {
          oprot.writeI32(struct.partitionSpec.size());
          for (PartitionSpec _iter1196 : struct.partitionSpec)
          {
            _iter1196.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, GetPartitionsResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list1197 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.partitionSpec = new ArrayList<PartitionSpec>(_list1197.size);
          PartitionSpec _elem1198;
          for (int _i1199 = 0; _i1199 < _list1197.size; ++_i1199)
          {
            _elem1198 = new PartitionSpec();
            _elem1198.read(iprot);
            struct.partitionSpec.add(_elem1198);
          }
        }
        struct.setPartitionSpecIsSet(true);
      }
    }
  }

}

