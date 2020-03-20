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
@org.apache.hadoop.classification.InterfaceAudience.Public @org.apache.hadoop.classification.InterfaceStability.Stable public class ExtendedTableInfo implements org.apache.thrift.TBase<ExtendedTableInfo, ExtendedTableInfo._Fields>, java.io.Serializable, Cloneable, Comparable<ExtendedTableInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ExtendedTableInfo");

  private static final org.apache.thrift.protocol.TField TBL_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("tblName", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField ACCESS_TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("accessType", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField REQUIRED_READ_CAPABILITIES_FIELD_DESC = new org.apache.thrift.protocol.TField("requiredReadCapabilities", org.apache.thrift.protocol.TType.LIST, (short)3);
  private static final org.apache.thrift.protocol.TField REQUIRED_WRITE_CAPABILITIES_FIELD_DESC = new org.apache.thrift.protocol.TField("requiredWriteCapabilities", org.apache.thrift.protocol.TType.LIST, (short)4);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ExtendedTableInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ExtendedTableInfoTupleSchemeFactory());
  }

  private String tblName; // required
  private int accessType; // optional
  private List<String> requiredReadCapabilities; // optional
  private List<String> requiredWriteCapabilities; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    TBL_NAME((short)1, "tblName"),
    ACCESS_TYPE((short)2, "accessType"),
    REQUIRED_READ_CAPABILITIES((short)3, "requiredReadCapabilities"),
    REQUIRED_WRITE_CAPABILITIES((short)4, "requiredWriteCapabilities");

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
        case 1: // TBL_NAME
          return TBL_NAME;
        case 2: // ACCESS_TYPE
          return ACCESS_TYPE;
        case 3: // REQUIRED_READ_CAPABILITIES
          return REQUIRED_READ_CAPABILITIES;
        case 4: // REQUIRED_WRITE_CAPABILITIES
          return REQUIRED_WRITE_CAPABILITIES;
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
  private static final int __ACCESSTYPE_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.ACCESS_TYPE,_Fields.REQUIRED_READ_CAPABILITIES,_Fields.REQUIRED_WRITE_CAPABILITIES};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TBL_NAME, new org.apache.thrift.meta_data.FieldMetaData("tblName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ACCESS_TYPE, new org.apache.thrift.meta_data.FieldMetaData("accessType", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.REQUIRED_READ_CAPABILITIES, new org.apache.thrift.meta_data.FieldMetaData("requiredReadCapabilities", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    tmpMap.put(_Fields.REQUIRED_WRITE_CAPABILITIES, new org.apache.thrift.meta_data.FieldMetaData("requiredWriteCapabilities", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ExtendedTableInfo.class, metaDataMap);
  }

  public ExtendedTableInfo() {
  }

  public ExtendedTableInfo(
    String tblName)
  {
    this();
    this.tblName = tblName;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ExtendedTableInfo(ExtendedTableInfo other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetTblName()) {
      this.tblName = other.tblName;
    }
    this.accessType = other.accessType;
    if (other.isSetRequiredReadCapabilities()) {
      List<String> __this__requiredReadCapabilities = new ArrayList<String>(other.requiredReadCapabilities);
      this.requiredReadCapabilities = __this__requiredReadCapabilities;
    }
    if (other.isSetRequiredWriteCapabilities()) {
      List<String> __this__requiredWriteCapabilities = new ArrayList<String>(other.requiredWriteCapabilities);
      this.requiredWriteCapabilities = __this__requiredWriteCapabilities;
    }
  }

  public ExtendedTableInfo deepCopy() {
    return new ExtendedTableInfo(this);
  }

  @Override
  public void clear() {
    this.tblName = null;
    setAccessTypeIsSet(false);
    this.accessType = 0;
    this.requiredReadCapabilities = null;
    this.requiredWriteCapabilities = null;
  }

  public String getTblName() {
    return this.tblName;
  }

  public void setTblName(String tblName) {
    this.tblName = tblName;
  }

  public void unsetTblName() {
    this.tblName = null;
  }

  /** Returns true if field tblName is set (has been assigned a value) and false otherwise */
  public boolean isSetTblName() {
    return this.tblName != null;
  }

  public void setTblNameIsSet(boolean value) {
    if (!value) {
      this.tblName = null;
    }
  }

  public int getAccessType() {
    return this.accessType;
  }

  public void setAccessType(int accessType) {
    this.accessType = accessType;
    setAccessTypeIsSet(true);
  }

  public void unsetAccessType() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ACCESSTYPE_ISSET_ID);
  }

  /** Returns true if field accessType is set (has been assigned a value) and false otherwise */
  public boolean isSetAccessType() {
    return EncodingUtils.testBit(__isset_bitfield, __ACCESSTYPE_ISSET_ID);
  }

  public void setAccessTypeIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ACCESSTYPE_ISSET_ID, value);
  }

  public int getRequiredReadCapabilitiesSize() {
    return (this.requiredReadCapabilities == null) ? 0 : this.requiredReadCapabilities.size();
  }

  public java.util.Iterator<String> getRequiredReadCapabilitiesIterator() {
    return (this.requiredReadCapabilities == null) ? null : this.requiredReadCapabilities.iterator();
  }

  public void addToRequiredReadCapabilities(String elem) {
    if (this.requiredReadCapabilities == null) {
      this.requiredReadCapabilities = new ArrayList<String>();
    }
    this.requiredReadCapabilities.add(elem);
  }

  public List<String> getRequiredReadCapabilities() {
    return this.requiredReadCapabilities;
  }

  public void setRequiredReadCapabilities(List<String> requiredReadCapabilities) {
    this.requiredReadCapabilities = requiredReadCapabilities;
  }

  public void unsetRequiredReadCapabilities() {
    this.requiredReadCapabilities = null;
  }

  /** Returns true if field requiredReadCapabilities is set (has been assigned a value) and false otherwise */
  public boolean isSetRequiredReadCapabilities() {
    return this.requiredReadCapabilities != null;
  }

  public void setRequiredReadCapabilitiesIsSet(boolean value) {
    if (!value) {
      this.requiredReadCapabilities = null;
    }
  }

  public int getRequiredWriteCapabilitiesSize() {
    return (this.requiredWriteCapabilities == null) ? 0 : this.requiredWriteCapabilities.size();
  }

  public java.util.Iterator<String> getRequiredWriteCapabilitiesIterator() {
    return (this.requiredWriteCapabilities == null) ? null : this.requiredWriteCapabilities.iterator();
  }

  public void addToRequiredWriteCapabilities(String elem) {
    if (this.requiredWriteCapabilities == null) {
      this.requiredWriteCapabilities = new ArrayList<String>();
    }
    this.requiredWriteCapabilities.add(elem);
  }

  public List<String> getRequiredWriteCapabilities() {
    return this.requiredWriteCapabilities;
  }

  public void setRequiredWriteCapabilities(List<String> requiredWriteCapabilities) {
    this.requiredWriteCapabilities = requiredWriteCapabilities;
  }

  public void unsetRequiredWriteCapabilities() {
    this.requiredWriteCapabilities = null;
  }

  /** Returns true if field requiredWriteCapabilities is set (has been assigned a value) and false otherwise */
  public boolean isSetRequiredWriteCapabilities() {
    return this.requiredWriteCapabilities != null;
  }

  public void setRequiredWriteCapabilitiesIsSet(boolean value) {
    if (!value) {
      this.requiredWriteCapabilities = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case TBL_NAME:
      if (value == null) {
        unsetTblName();
      } else {
        setTblName((String)value);
      }
      break;

    case ACCESS_TYPE:
      if (value == null) {
        unsetAccessType();
      } else {
        setAccessType((Integer)value);
      }
      break;

    case REQUIRED_READ_CAPABILITIES:
      if (value == null) {
        unsetRequiredReadCapabilities();
      } else {
        setRequiredReadCapabilities((List<String>)value);
      }
      break;

    case REQUIRED_WRITE_CAPABILITIES:
      if (value == null) {
        unsetRequiredWriteCapabilities();
      } else {
        setRequiredWriteCapabilities((List<String>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case TBL_NAME:
      return getTblName();

    case ACCESS_TYPE:
      return getAccessType();

    case REQUIRED_READ_CAPABILITIES:
      return getRequiredReadCapabilities();

    case REQUIRED_WRITE_CAPABILITIES:
      return getRequiredWriteCapabilities();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case TBL_NAME:
      return isSetTblName();
    case ACCESS_TYPE:
      return isSetAccessType();
    case REQUIRED_READ_CAPABILITIES:
      return isSetRequiredReadCapabilities();
    case REQUIRED_WRITE_CAPABILITIES:
      return isSetRequiredWriteCapabilities();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ExtendedTableInfo)
      return this.equals((ExtendedTableInfo)that);
    return false;
  }

  public boolean equals(ExtendedTableInfo that) {
    if (that == null)
      return false;

    boolean this_present_tblName = true && this.isSetTblName();
    boolean that_present_tblName = true && that.isSetTblName();
    if (this_present_tblName || that_present_tblName) {
      if (!(this_present_tblName && that_present_tblName))
        return false;
      if (!this.tblName.equals(that.tblName))
        return false;
    }

    boolean this_present_accessType = true && this.isSetAccessType();
    boolean that_present_accessType = true && that.isSetAccessType();
    if (this_present_accessType || that_present_accessType) {
      if (!(this_present_accessType && that_present_accessType))
        return false;
      if (this.accessType != that.accessType)
        return false;
    }

    boolean this_present_requiredReadCapabilities = true && this.isSetRequiredReadCapabilities();
    boolean that_present_requiredReadCapabilities = true && that.isSetRequiredReadCapabilities();
    if (this_present_requiredReadCapabilities || that_present_requiredReadCapabilities) {
      if (!(this_present_requiredReadCapabilities && that_present_requiredReadCapabilities))
        return false;
      if (!this.requiredReadCapabilities.equals(that.requiredReadCapabilities))
        return false;
    }

    boolean this_present_requiredWriteCapabilities = true && this.isSetRequiredWriteCapabilities();
    boolean that_present_requiredWriteCapabilities = true && that.isSetRequiredWriteCapabilities();
    if (this_present_requiredWriteCapabilities || that_present_requiredWriteCapabilities) {
      if (!(this_present_requiredWriteCapabilities && that_present_requiredWriteCapabilities))
        return false;
      if (!this.requiredWriteCapabilities.equals(that.requiredWriteCapabilities))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_tblName = true && (isSetTblName());
    list.add(present_tblName);
    if (present_tblName)
      list.add(tblName);

    boolean present_accessType = true && (isSetAccessType());
    list.add(present_accessType);
    if (present_accessType)
      list.add(accessType);

    boolean present_requiredReadCapabilities = true && (isSetRequiredReadCapabilities());
    list.add(present_requiredReadCapabilities);
    if (present_requiredReadCapabilities)
      list.add(requiredReadCapabilities);

    boolean present_requiredWriteCapabilities = true && (isSetRequiredWriteCapabilities());
    list.add(present_requiredWriteCapabilities);
    if (present_requiredWriteCapabilities)
      list.add(requiredWriteCapabilities);

    return list.hashCode();
  }

  @Override
  public int compareTo(ExtendedTableInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetTblName()).compareTo(other.isSetTblName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTblName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tblName, other.tblName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetAccessType()).compareTo(other.isSetAccessType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAccessType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.accessType, other.accessType);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetRequiredReadCapabilities()).compareTo(other.isSetRequiredReadCapabilities());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRequiredReadCapabilities()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.requiredReadCapabilities, other.requiredReadCapabilities);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetRequiredWriteCapabilities()).compareTo(other.isSetRequiredWriteCapabilities());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRequiredWriteCapabilities()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.requiredWriteCapabilities, other.requiredWriteCapabilities);
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
    StringBuilder sb = new StringBuilder("ExtendedTableInfo(");
    boolean first = true;

    sb.append("tblName:");
    if (this.tblName == null) {
      sb.append("null");
    } else {
      sb.append(this.tblName);
    }
    first = false;
    if (isSetAccessType()) {
      if (!first) sb.append(", ");
      sb.append("accessType:");
      sb.append(this.accessType);
      first = false;
    }
    if (isSetRequiredReadCapabilities()) {
      if (!first) sb.append(", ");
      sb.append("requiredReadCapabilities:");
      if (this.requiredReadCapabilities == null) {
        sb.append("null");
      } else {
        sb.append(this.requiredReadCapabilities);
      }
      first = false;
    }
    if (isSetRequiredWriteCapabilities()) {
      if (!first) sb.append(", ");
      sb.append("requiredWriteCapabilities:");
      if (this.requiredWriteCapabilities == null) {
        sb.append("null");
      } else {
        sb.append(this.requiredWriteCapabilities);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (!isSetTblName()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'tblName' is unset! Struct:" + toString());
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

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ExtendedTableInfoStandardSchemeFactory implements SchemeFactory {
    public ExtendedTableInfoStandardScheme getScheme() {
      return new ExtendedTableInfoStandardScheme();
    }
  }

  private static class ExtendedTableInfoStandardScheme extends StandardScheme<ExtendedTableInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ExtendedTableInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TBL_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.tblName = iprot.readString();
              struct.setTblNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // ACCESS_TYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.accessType = iprot.readI32();
              struct.setAccessTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // REQUIRED_READ_CAPABILITIES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list992 = iprot.readListBegin();
                struct.requiredReadCapabilities = new ArrayList<String>(_list992.size);
                String _elem993;
                for (int _i994 = 0; _i994 < _list992.size; ++_i994)
                {
                  _elem993 = iprot.readString();
                  struct.requiredReadCapabilities.add(_elem993);
                }
                iprot.readListEnd();
              }
              struct.setRequiredReadCapabilitiesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // REQUIRED_WRITE_CAPABILITIES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list995 = iprot.readListBegin();
                struct.requiredWriteCapabilities = new ArrayList<String>(_list995.size);
                String _elem996;
                for (int _i997 = 0; _i997 < _list995.size; ++_i997)
                {
                  _elem996 = iprot.readString();
                  struct.requiredWriteCapabilities.add(_elem996);
                }
                iprot.readListEnd();
              }
              struct.setRequiredWriteCapabilitiesIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, ExtendedTableInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.tblName != null) {
        oprot.writeFieldBegin(TBL_NAME_FIELD_DESC);
        oprot.writeString(struct.tblName);
        oprot.writeFieldEnd();
      }
      if (struct.isSetAccessType()) {
        oprot.writeFieldBegin(ACCESS_TYPE_FIELD_DESC);
        oprot.writeI32(struct.accessType);
        oprot.writeFieldEnd();
      }
      if (struct.requiredReadCapabilities != null) {
        if (struct.isSetRequiredReadCapabilities()) {
          oprot.writeFieldBegin(REQUIRED_READ_CAPABILITIES_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.requiredReadCapabilities.size()));
            for (String _iter998 : struct.requiredReadCapabilities)
            {
              oprot.writeString(_iter998);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      if (struct.requiredWriteCapabilities != null) {
        if (struct.isSetRequiredWriteCapabilities()) {
          oprot.writeFieldBegin(REQUIRED_WRITE_CAPABILITIES_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.requiredWriteCapabilities.size()));
            for (String _iter999 : struct.requiredWriteCapabilities)
            {
              oprot.writeString(_iter999);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ExtendedTableInfoTupleSchemeFactory implements SchemeFactory {
    public ExtendedTableInfoTupleScheme getScheme() {
      return new ExtendedTableInfoTupleScheme();
    }
  }

  private static class ExtendedTableInfoTupleScheme extends TupleScheme<ExtendedTableInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ExtendedTableInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeString(struct.tblName);
      BitSet optionals = new BitSet();
      if (struct.isSetAccessType()) {
        optionals.set(0);
      }
      if (struct.isSetRequiredReadCapabilities()) {
        optionals.set(1);
      }
      if (struct.isSetRequiredWriteCapabilities()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetAccessType()) {
        oprot.writeI32(struct.accessType);
      }
      if (struct.isSetRequiredReadCapabilities()) {
        {
          oprot.writeI32(struct.requiredReadCapabilities.size());
          for (String _iter1000 : struct.requiredReadCapabilities)
          {
            oprot.writeString(_iter1000);
          }
        }
      }
      if (struct.isSetRequiredWriteCapabilities()) {
        {
          oprot.writeI32(struct.requiredWriteCapabilities.size());
          for (String _iter1001 : struct.requiredWriteCapabilities)
          {
            oprot.writeString(_iter1001);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ExtendedTableInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.tblName = iprot.readString();
      struct.setTblNameIsSet(true);
      BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.accessType = iprot.readI32();
        struct.setAccessTypeIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list1002 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, iprot.readI32());
          struct.requiredReadCapabilities = new ArrayList<String>(_list1002.size);
          String _elem1003;
          for (int _i1004 = 0; _i1004 < _list1002.size; ++_i1004)
          {
            _elem1003 = iprot.readString();
            struct.requiredReadCapabilities.add(_elem1003);
          }
        }
        struct.setRequiredReadCapabilitiesIsSet(true);
      }
      if (incoming.get(2)) {
        {
          org.apache.thrift.protocol.TList _list1005 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, iprot.readI32());
          struct.requiredWriteCapabilities = new ArrayList<String>(_list1005.size);
          String _elem1006;
          for (int _i1007 = 0; _i1007 < _list1005.size; ++_i1007)
          {
            _elem1006 = iprot.readString();
            struct.requiredWriteCapabilities.add(_elem1006);
          }
        }
        struct.setRequiredWriteCapabilitiesIsSet(true);
      }
    }
  }

}

