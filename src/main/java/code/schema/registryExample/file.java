/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package registryExample;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class file extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -2722943878382465553L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"file\",\"namespace\":\"registryExample\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"buffer\",\"type\":\"bytes\"},{\"name\":\"extension\",\"type\":\"string\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<file> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<file> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<file> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<file> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<file> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this file to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a file from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a file instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static file fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private java.lang.CharSequence name;
  private java.nio.ByteBuffer buffer;
  private java.lang.CharSequence extension;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public file() {}

  /**
   * All-args constructor.
   * @param name The new value for name
   * @param buffer The new value for buffer
   * @param extension The new value for extension
   */
  public file(java.lang.CharSequence name, java.nio.ByteBuffer buffer, java.lang.CharSequence extension) {
    this.name = name;
    this.buffer = buffer;
    this.extension = extension;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return name;
    case 1: return buffer;
    case 2: return extension;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: name = (java.lang.CharSequence)value$; break;
    case 1: buffer = (java.nio.ByteBuffer)value$; break;
    case 2: extension = (java.lang.CharSequence)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'name' field.
   * @return The value of the 'name' field.
   */
  public java.lang.CharSequence getName() {
    return name;
  }


  /**
   * Sets the value of the 'name' field.
   * @param value the value to set.
   */
  public void setName(java.lang.CharSequence value) {
    this.name = value;
  }

  /**
   * Gets the value of the 'buffer' field.
   * @return The value of the 'buffer' field.
   */
  public java.nio.ByteBuffer getBuffer() {
    return buffer;
  }


  /**
   * Sets the value of the 'buffer' field.
   * @param value the value to set.
   */
  public void setBuffer(java.nio.ByteBuffer value) {
    this.buffer = value;
  }

  /**
   * Gets the value of the 'extension' field.
   * @return The value of the 'extension' field.
   */
  public java.lang.CharSequence getExtension() {
    return extension;
  }


  /**
   * Sets the value of the 'extension' field.
   * @param value the value to set.
   */
  public void setExtension(java.lang.CharSequence value) {
    this.extension = value;
  }

  /**
   * Creates a new file RecordBuilder.
   * @return A new file RecordBuilder
   */
  public static registryExample.file.Builder newBuilder() {
    return new registryExample.file.Builder();
  }

  /**
   * Creates a new file RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new file RecordBuilder
   */
  public static registryExample.file.Builder newBuilder(registryExample.file.Builder other) {
    if (other == null) {
      return new registryExample.file.Builder();
    } else {
      return new registryExample.file.Builder(other);
    }
  }

  /**
   * Creates a new file RecordBuilder by copying an existing file instance.
   * @param other The existing instance to copy.
   * @return A new file RecordBuilder
   */
  public static registryExample.file.Builder newBuilder(registryExample.file other) {
    if (other == null) {
      return new registryExample.file.Builder();
    } else {
      return new registryExample.file.Builder(other);
    }
  }

  /**
   * RecordBuilder for file instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<file>
    implements org.apache.avro.data.RecordBuilder<file> {

    private java.lang.CharSequence name;
    private java.nio.ByteBuffer buffer;
    private java.lang.CharSequence extension;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(registryExample.file.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.name)) {
        this.name = data().deepCopy(fields()[0].schema(), other.name);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.buffer)) {
        this.buffer = data().deepCopy(fields()[1].schema(), other.buffer);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.extension)) {
        this.extension = data().deepCopy(fields()[2].schema(), other.extension);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
    }

    /**
     * Creates a Builder by copying an existing file instance
     * @param other The existing instance to copy.
     */
    private Builder(registryExample.file other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.name)) {
        this.name = data().deepCopy(fields()[0].schema(), other.name);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.buffer)) {
        this.buffer = data().deepCopy(fields()[1].schema(), other.buffer);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.extension)) {
        this.extension = data().deepCopy(fields()[2].schema(), other.extension);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'name' field.
      * @return The value.
      */
    public java.lang.CharSequence getName() {
      return name;
    }


    /**
      * Sets the value of the 'name' field.
      * @param value The value of 'name'.
      * @return This builder.
      */
    public registryExample.file.Builder setName(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.name = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'name' field has been set.
      * @return True if the 'name' field has been set, false otherwise.
      */
    public boolean hasName() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'name' field.
      * @return This builder.
      */
    public registryExample.file.Builder clearName() {
      name = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'buffer' field.
      * @return The value.
      */
    public java.nio.ByteBuffer getBuffer() {
      return buffer;
    }


    /**
      * Sets the value of the 'buffer' field.
      * @param value The value of 'buffer'.
      * @return This builder.
      */
    public registryExample.file.Builder setBuffer(java.nio.ByteBuffer value) {
      validate(fields()[1], value);
      this.buffer = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'buffer' field has been set.
      * @return True if the 'buffer' field has been set, false otherwise.
      */
    public boolean hasBuffer() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'buffer' field.
      * @return This builder.
      */
    public registryExample.file.Builder clearBuffer() {
      buffer = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'extension' field.
      * @return The value.
      */
    public java.lang.CharSequence getExtension() {
      return extension;
    }


    /**
      * Sets the value of the 'extension' field.
      * @param value The value of 'extension'.
      * @return This builder.
      */
    public registryExample.file.Builder setExtension(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.extension = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'extension' field has been set.
      * @return True if the 'extension' field has been set, false otherwise.
      */
    public boolean hasExtension() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'extension' field.
      * @return This builder.
      */
    public registryExample.file.Builder clearExtension() {
      extension = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public file build() {
      try {
        file record = new file();
        record.name = fieldSetFlags()[0] ? this.name : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.buffer = fieldSetFlags()[1] ? this.buffer : (java.nio.ByteBuffer) defaultValue(fields()[1]);
        record.extension = fieldSetFlags()[2] ? this.extension : (java.lang.CharSequence) defaultValue(fields()[2]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<file>
    WRITER$ = (org.apache.avro.io.DatumWriter<file>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<file>
    READER$ = (org.apache.avro.io.DatumReader<file>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.name);

    out.writeBytes(this.buffer);

    out.writeString(this.extension);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.name = in.readString(this.name instanceof Utf8 ? (Utf8)this.name : null);

      this.buffer = in.readBytes(this.buffer);

      this.extension = in.readString(this.extension instanceof Utf8 ? (Utf8)this.extension : null);

    } else {
      for (int i = 0; i < 3; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.name = in.readString(this.name instanceof Utf8 ? (Utf8)this.name : null);
          break;

        case 1:
          this.buffer = in.readBytes(this.buffer);
          break;

        case 2:
          this.extension = in.readString(this.extension instanceof Utf8 ? (Utf8)this.extension : null);
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










