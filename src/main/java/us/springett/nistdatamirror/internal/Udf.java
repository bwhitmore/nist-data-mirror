/* This file is Copyright (c) 2017 Brent Whitmore. All Rights Reserved.
 *
 * This file is part of nist-data-mirror.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 // This is a generated file! Please edit source .ksy file and use kaitai-struct-compiler to rebuild,
 // and then make the corrections noted in the immediately-following lines.

package us.springett.nistdatamirror.internal; // Added

// import io.kaitai.struct.KaitaiStruct;  // Removed
// import io.kaitai.struct.KaitaiStream;  // Removed

import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.Charset;

public class Udf extends KaitaiStruct {
    public static Udf fromFile(String fileName) throws IOException {
        return new Udf(new KaitaiStream(fileName));
    }

    public enum TagIdentifier {
        PRIMARY_VOLUME_DESCRIPTOR(1),
        ANCHOR_VOLUME_DESCRIPTOR_POINTER(2),
        VOLUME_DESCRIPTOR_POINTER(3),
        IMPLEMENTATION_USE_VOLUME_DESCRIPTOR(4),
        PARTITION_DESCRIPTOR(5),
        LOGICAL_VOLUME_DESCRIPTOR(6),
        UNALLOCATED_SPACE_DESCRIPTOR(7),
        TERMINATING_DESCRIPTOR(8),
        LOGICAL_VOLUME_INTEGRITY_DESCRIPTOR(9),
        FILE_SET_DESCRIPTOR(256),
        FILE_IDENTIFIER_DESCRIPTOR(257),
        ALLOCATION_EXTENT_DESCRIPTOR(258),
        INDIRECT_ENTRY(259),
        TERMINAL_ENTRY(260),
        FILE_ENTRY(261),
        EXTENDED_ATTRIBUTE_HEADER_DESCRIPTOR(262),
        UNALLOCATED_SPACE_ENTRY(263),
        SPACE_BITMAP_DESCRIPTOR(264),
        PARTITION_INTEGRITY_ENTRY(265),
        EXTENDED_FILE_ENTRY(266);

        private final long id;
        TagIdentifier(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, TagIdentifier> byId = new HashMap<Long, TagIdentifier>(20);
        static {
            for (TagIdentifier e : TagIdentifier.values())
                byId.put(e.id(), e);
        }
        public static TagIdentifier byId(long id) { return byId.get(id); }
    }

    public enum ExtentClass {
        EXTENT_RECORDED_ALLOCATED(0),
        EXTENT_ALLOCATED_BUT_NOT_RECORDED(1),
        EXTENT_NEITHER_ALLOCATED_NOR_RECORDED(2),
        NEXT_EXTENT_OF_ALLOCATION_DESCRIPTORS(3);

        private final long id;
        ExtentClass(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, ExtentClass> byId = new HashMap<Long, ExtentClass>(4);
        static {
            for (ExtentClass e : ExtentClass.values())
                byId.put(e.id(), e);
        }
        public static ExtentClass byId(long id) { return byId.get(id); }
    }

    public enum IcbFileType {
        UNSPECIFIED(0),
        UNALLOCATED_SPACE_ENTRY(1),
        PARTITION_INTEGRITY_ENTRY(2),
        INDIRECT_ENTRY(3),
        DIRECTORY(4),
        BYTE_STREAM(5),
        BLOCK_SPECIAL_DEVICE(6),
        CHARACTER_SPECIAL_DEVICE(7),
        EXTENDED_ATTRIBUTES(8),
        PIPE(9),
        SOCKET(10),
        TERMINAL(11),
        SYMBOLIC_LINK(12),
        STREAM_DIRECTORY(13),
        AGREEMENT_248(248),
        AGREEMENT_249(249),
        AGREEMENT_250(250),
        AGREEMENT_251(251),
        AGREEMENT_252(252),
        AGREEMENT_253(253),
        AGREEMENT_254(254),
        AGREEMENT_255(255);

        private final long id;
        IcbFileType(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, IcbFileType> byId = new HashMap<Long, IcbFileType>(22);
        static {
            for (IcbFileType e : IcbFileType.values())
                byId.put(e.id(), e);
        }
        public static IcbFileType byId(long id) { return byId.get(id); }
    }

    public enum IcbDescUse {
        USE_SHORT_AD(0),
        USE_LONG_AD(1),
        USE_EXTENDED_AD(2),
        IMMEDIATE(3);

        private final long id;
        IcbDescUse(long id) { this.id = id; }
        public long id() { return id; }
        private static final Map<Long, IcbDescUse> byId = new HashMap<Long, IcbDescUse>(4);
        static {
            for (IcbDescUse e : IcbDescUse.values())
                byId.put(e.id(), e);
        }
        public static IcbDescUse byId(long id) { return byId.get(id); }
    }

    public Udf(KaitaiStream _io) {
        super(_io);
        this._root = this;
        _read();
    }

    public Udf(KaitaiStream _io, KaitaiStruct _parent) {
        super(_io);
        this._parent = _parent;
        this._root = this;
        _read();
    }

    public Udf(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
        super(_io);
        this._parent = _parent;
        this._root = _root;
        _read();
    }
    private void _read() {
    }
    public static class UdfVolDesc3 extends KaitaiStruct {
        public static UdfVolDesc3 fromFile(String fileName) throws IOException {
            return new UdfVolDesc3(new KaitaiStream(fileName));
        }

        public UdfVolDesc3(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public UdfVolDesc3(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public UdfVolDesc3(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.type = this._io.readU1();
            this.magic = this._io.ensureFixedContents(new byte[] { 84, 69, 65, 48, 49 });
        }
        private int type;
        private byte[] magic;
        private Udf _root;
        private KaitaiStruct _parent;
        public int type() { return type; }
        public byte[] magic() { return magic; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class Timestamp extends KaitaiStruct {
        public static Timestamp fromFile(String fileName) throws IOException {
            return new Timestamp(new KaitaiStream(fileName));
        }

        public Timestamp(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public Timestamp(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public Timestamp(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.typeAndTz = this._io.readU2le();
            this.year = this._io.readS2le();
            this.month = this._io.readU1();
            this.day = this._io.readU1();
            this.hour = this._io.readU1();
            this.minute = this._io.readU1();
            this.second = this._io.readU1();
            this.centiseconds = this._io.readU1();
            this.hundredsOfMicroseconds = this._io.readU1();
            this.microseconds = this._io.readU1();
        }
        private int typeAndTz;
        private short year;
        private int month;
        private int day;
        private int hour;
        private int minute;
        private int second;
        private int centiseconds;
        private int hundredsOfMicroseconds;
        private int microseconds;
        private Udf _root;
        private KaitaiStruct _parent;
        public int typeAndTz() { return typeAndTz; }
        public short year() { return year; }
        public int month() { return month; }
        public int day() { return day; }
        public int hour() { return hour; }
        public int minute() { return minute; }
        public int second() { return second; }
        public int centiseconds() { return centiseconds; }
        public int hundredsOfMicroseconds() { return hundredsOfMicroseconds; }
        public int microseconds() { return microseconds; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class DescriptorTag extends KaitaiStruct {
        public static DescriptorTag fromFile(String fileName) throws IOException {
            return new DescriptorTag(new KaitaiStream(fileName));
        }

        public DescriptorTag(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public DescriptorTag(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public DescriptorTag(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.tagId = Udf.TagIdentifier.byId(this._io.readU2le());
            this.descVer = this._io.readU2le();
            this.tagCksum = this._io.readU1();
            this.reserved = this._io.readU1();
            this.tagSerialNum = this._io.readU2le();
            this.descCrc = this._io.readU2le();
            this.descSerialNumLength = this._io.readU2le();
            this.tagLocation = this._io.readU4le();
        }
        private TagIdentifier tagId;
        private int descVer;
        private int tagCksum;
        private int reserved;
        private int tagSerialNum;
        private int descCrc;
        private int descSerialNumLength;
        private long tagLocation;
        private Udf _root;
        private KaitaiStruct _parent;
        public TagIdentifier tagId() { return tagId; }
        public int descVer() { return descVer; }
        public int tagCksum() { return tagCksum; }
        public int reserved() { return reserved; }
        public int tagSerialNum() { return tagSerialNum; }
        public int descCrc() { return descCrc; }
        public int descSerialNumLength() { return descSerialNumLength; }
        public long tagLocation() { return tagLocation; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class Icbtag extends KaitaiStruct {
        public static Icbtag fromFile(String fileName) throws IOException {
            return new Icbtag(new KaitaiStream(fileName));
        }

        public Icbtag(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public Icbtag(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public Icbtag(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.priorRecordedNumOfDirectEntries = this._io.readU4le();
            this.strategyType = this._io.readU2le();
            this.strategyParameter = this._io.readBytes(2);
            this.maxNumOfEntries = this._io.readU2le();
            this.reserved1 = this._io.readBytes(1);
            this.fileType = Udf.IcbFileType.byId(this._io.readU1());
            this.parentIcbLocation = new LbAddr(this._io, this, _root);
            this.flags = this._io.readU2le();
        }
        private IcbDescUse descriptorUse;
        public IcbDescUse descriptorUse() {
            if (this.descriptorUse != null)
                return this.descriptorUse;
            this.descriptorUse = Udf.IcbDescUse.byId((flags() & 7));
            return this.descriptorUse;
        }
        private Boolean isDir;
        public Boolean isDir() {
            if (this.isDir != null)
                return this.isDir;
            boolean _tmp = (boolean) ((flags() & 8) != 0);
            this.isDir = _tmp;
            return this.isDir;
        }
        private Boolean archive;
        public Boolean archive() {
            if (this.archive != null)
                return this.archive;
            boolean _tmp = (boolean) ((flags() & 32) != 0);
            this.archive = _tmp;
            return this.archive;
        }
        private Boolean transformed;
        public Boolean transformed() {
            if (this.transformed != null)
                return this.transformed;
            boolean _tmp = (boolean) ((flags() & 2048) != 0);
            this.transformed = _tmp;
            return this.transformed;
        }
        private Boolean doNotRelocate;
        public Boolean doNotRelocate() {
            if (this.doNotRelocate != null)
                return this.doNotRelocate;
            boolean _tmp = (boolean) ((flags() & 16) != 0);
            this.doNotRelocate = _tmp;
            return this.doNotRelocate;
        }
        private Boolean sticky;
        public Boolean sticky() {
            if (this.sticky != null)
                return this.sticky;
            boolean _tmp = (boolean) ((flags() & 256) != 0);
            this.sticky = _tmp;
            return this.sticky;
        }
        private Boolean setgid;
        public Boolean setgid() {
            if (this.setgid != null)
                return this.setgid;
            boolean _tmp = (boolean) ((flags() & 128) != 0);
            this.setgid = _tmp;
            return this.setgid;
        }
        private Boolean system;
        public Boolean system() {
            if (this.system != null)
                return this.system;
            boolean _tmp = (boolean) ((flags() & 1024) != 0);
            this.system = _tmp;
            return this.system;
        }
        private Boolean isStream;
        public Boolean isStream() {
            if (this.isStream != null)
                return this.isStream;
            boolean _tmp = (boolean) ((flags() & 8192) != 0);
            this.isStream = _tmp;
            return this.isStream;
        }
        private Boolean multiversions;
        public Boolean multiversions() {
            if (this.multiversions != null)
                return this.multiversions;
            boolean _tmp = (boolean) ((flags() & 4096) != 0);
            this.multiversions = _tmp;
            return this.multiversions;
        }
        private Boolean setuid;
        public Boolean setuid() {
            if (this.setuid != null)
                return this.setuid;
            boolean _tmp = (boolean) ((flags() & 64) != 0);
            this.setuid = _tmp;
            return this.setuid;
        }
        private Boolean contiguous;
        public Boolean contiguous() {
            if (this.contiguous != null)
                return this.contiguous;
            boolean _tmp = (boolean) ((flags() & 512) != 0);
            this.contiguous = _tmp;
            return this.contiguous;
        }
        private long priorRecordedNumOfDirectEntries;
        private int strategyType;
        private byte[] strategyParameter;
        private int maxNumOfEntries;
        private byte[] reserved1;
        private IcbFileType fileType;
        private LbAddr parentIcbLocation;
        private int flags;
        private Udf _root;
        private KaitaiStruct _parent;
        public long priorRecordedNumOfDirectEntries() { return priorRecordedNumOfDirectEntries; }
        public int strategyType() { return strategyType; }
        public byte[] strategyParameter() { return strategyParameter; }
        public int maxNumOfEntries() { return maxNumOfEntries; }
        public byte[] reserved1() { return reserved1; }
        public IcbFileType fileType() { return fileType; }
        public LbAddr parentIcbLocation() { return parentIcbLocation; }
        public int flags() { return flags; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class FileSetDescriptor extends KaitaiStruct {
        public static FileSetDescriptor fromFile(String fileName) throws IOException {
            return new FileSetDescriptor(new KaitaiStream(fileName));
        }

        public FileSetDescriptor(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public FileSetDescriptor(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public FileSetDescriptor(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.tag = new FileSetDescriptorTag(this._io, this, _root);
            this.recordingDateAndTime = new Timestamp(this._io, this, _root);
            this.interchangeLevel = this._io.readU2le();
            this.maxInterchangeLevel = this._io.readU2le();
            this.characterSetList = this._io.readU4le();
            this.maxCharacterSetList = this._io.readU4le();
            this.fileSetNum = this._io.readU4le();
            this.fileSetDescNum = this._io.readU4le();
            this.logicalVolumeIdCharSet = new Charspec(this._io, this, _root);
            this.logicalVolumeId = new Dstring128(this._io, this, _root);
            this.fileSetCharSet = new Charspec(this._io, this, _root);
            this.fileSetId = new Dstring32(this._io, this, _root);
            this.copyrightFileId = new Dstring32(this._io, this, _root);
            this.abstractFileId = new Dstring32(this._io, this, _root);
            this.rootDirectoryIcb = new LongAd(this._io, this, _root);
            this.domainId = new EntityId(this._io, this, _root);
            this.nextExtent = new LongAd(this._io, this, _root);
            this.systemStreamDirectoryIcb = new LongAd(this._io, this, _root);
            this.reserved = this._io.readBytes(32);
        }
        private FileSetDescriptorTag tag;
        private Timestamp recordingDateAndTime;
        private int interchangeLevel;
        private int maxInterchangeLevel;
        private long characterSetList;
        private long maxCharacterSetList;
        private long fileSetNum;
        private long fileSetDescNum;
        private Charspec logicalVolumeIdCharSet;
        private Dstring128 logicalVolumeId;
        private Charspec fileSetCharSet;
        private Dstring32 fileSetId;
        private Dstring32 copyrightFileId;
        private Dstring32 abstractFileId;
        private LongAd rootDirectoryIcb;
        private EntityId domainId;
        private LongAd nextExtent;
        private LongAd systemStreamDirectoryIcb;
        private byte[] reserved;
        private Udf _root;
        private KaitaiStruct _parent;
        public FileSetDescriptorTag tag() { return tag; }
        public Timestamp recordingDateAndTime() { return recordingDateAndTime; }
        public int interchangeLevel() { return interchangeLevel; }
        public int maxInterchangeLevel() { return maxInterchangeLevel; }
        public long characterSetList() { return characterSetList; }
        public long maxCharacterSetList() { return maxCharacterSetList; }
        public long fileSetNum() { return fileSetNum; }
        public long fileSetDescNum() { return fileSetDescNum; }
        public Charspec logicalVolumeIdCharSet() { return logicalVolumeIdCharSet; }
        public Dstring128 logicalVolumeId() { return logicalVolumeId; }
        public Charspec fileSetCharSet() { return fileSetCharSet; }
        public Dstring32 fileSetId() { return fileSetId; }
        public Dstring32 copyrightFileId() { return copyrightFileId; }
        public Dstring32 abstractFileId() { return abstractFileId; }
        public LongAd rootDirectoryIcb() { return rootDirectoryIcb; }
        public EntityId domainId() { return domainId; }
        public LongAd nextExtent() { return nextExtent; }
        public LongAd systemStreamDirectoryIcb() { return systemStreamDirectoryIcb; }
        public byte[] reserved() { return reserved; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class PartitionMap extends KaitaiStruct {
        public static PartitionMap fromFile(String fileName) throws IOException {
            return new PartitionMap(new KaitaiStream(fileName));
        }

        public PartitionMap(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public PartitionMap(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public PartitionMap(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.pmType = this._io.readU1();
            this.pmLength = this._io.readU8le();
            this.reserved1 = this._io.readBytes(2);
            this.partitionTypeId = new EntityId(this._io, this, _root);
            this.volumeSeqNum = this._io.readU2le();
            this.partitionNumber = this._io.readU2le();
            this.mapRecord = this._io.readBytes(24);
        }
        private int pmType;
        private long pmLength;
        private byte[] reserved1;
        private EntityId partitionTypeId;
        private int volumeSeqNum;
        private int partitionNumber;
        private byte[] mapRecord;
        private Udf _root;
        private KaitaiStruct _parent;
        public int pmType() { return pmType; }
        public long pmLength() { return pmLength; }
        public byte[] reserved1() { return reserved1; }
        public EntityId partitionTypeId() { return partitionTypeId; }
        public int volumeSeqNum() { return volumeSeqNum; }
        public int partitionNumber() { return partitionNumber; }
        public byte[] mapRecord() { return mapRecord; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class FileIdentifierDescriptor extends KaitaiStruct {
        public static FileIdentifierDescriptor fromFile(String fileName) throws IOException {
            return new FileIdentifierDescriptor(new KaitaiStream(fileName));
        }

        public FileIdentifierDescriptor(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public FileIdentifierDescriptor(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public FileIdentifierDescriptor(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.tag = new DescriptorTag(this._io, this, _root);
            this.fileVersionNum = this._io.readU2le();
            this.fileCharacteristics = this._io.readU1();
            this.fileIdLength = this._io.readU1();
            this.icbAd = new LongAd(this._io, this, _root);
            this.implUseLength = this._io.readU2le();
            this.implUse = this._io.readBytes(implUseLength());
            if (fileIdLength() > 0) {
                this.cs0Type = this._io.readU1();
            }
            if (isUtf8()) {
                this.fileIdUtf8 = new String(this._io.readBytes((fileIdLength() - 1)), Charset.forName("UTF-8"));
            }
            if (isUtf16()) {
                this.fileIdUtf16 = new String(this._io.readBytes((fileIdLength() - 1)), Charset.forName("UTF-16"));
            }
            if (isEmpty()) {
                this.fileIdEmpty = this._io.readBytes(0);
            }
            this.padding = this._io.readBytes(padSize());
        }
        private Integer unpaddedRecordSize;
        public Integer unpaddedRecordSize() {
            if (this.unpaddedRecordSize != null)
                return this.unpaddedRecordSize;
            int _tmp = (int) (((implUseLength() + fileIdLength()) + 38));
            this.unpaddedRecordSize = _tmp;
            return this.unpaddedRecordSize;
        }
        private Boolean isUtf8;
        public Boolean isUtf8() {
            if (this.isUtf8 != null)
                return this.isUtf8;
            boolean _tmp = (boolean) ( ((fileIdLength() > 0) && ( ((cs0Type() == 8) || (cs0Type() == 254)) )) );
            this.isUtf8 = _tmp;
            return this.isUtf8;
        }
        private Boolean isHidden;
        public Boolean isHidden() {
            if (this.isHidden != null)
                return this.isHidden;
            boolean _tmp = (boolean) ((fileCharacteristics() & 1) != 0);
            this.isHidden = _tmp;
            return this.isHidden;
        }
        private Boolean isDirectory;
        public Boolean isDirectory() {
            if (this.isDirectory != null)
                return this.isDirectory;
            boolean _tmp = (boolean) ((fileCharacteristics() & 2) != 0);
            this.isDirectory = _tmp;
            return this.isDirectory;
        }
        private Boolean isUtf16;
        public Boolean isUtf16() {
            if (this.isUtf16 != null)
                return this.isUtf16;
            boolean _tmp = (boolean) ( ((fileIdLength() > 0) && ( ((cs0Type() == 16) || (cs0Type() == 255)) )) );
            this.isUtf16 = _tmp;
            return this.isUtf16;
        }
        private Integer padSize;
        public Integer padSize() {
            if (this.padSize != null)
                return this.padSize;
            int _tmp = (int) (KaitaiStream.mod((4 - KaitaiStream.mod(unpaddedRecordSize(), 4)), 4));
            this.padSize = _tmp;
            return this.padSize;
        }
        private Boolean fileIsParentDir;
        public Boolean fileIsParentDir() {
            if (this.fileIsParentDir != null)
                return this.fileIsParentDir;
            boolean _tmp = (boolean) ((fileCharacteristics() & 8) != 0);
            this.fileIsParentDir = _tmp;
            return this.fileIsParentDir;
        }
        private Boolean fileStreamIsMeta;
        public Boolean fileStreamIsMeta() {
            if (this.fileStreamIsMeta != null)
                return this.fileStreamIsMeta;
            boolean _tmp = (boolean) ((fileCharacteristics() & 16) != 0);
            this.fileStreamIsMeta = _tmp;
            return this.fileStreamIsMeta;
        }
        private Integer recordSize;
        public Integer recordSize() {
            if (this.recordSize != null)
                return this.recordSize;
            int _tmp = (int) ((unpaddedRecordSize() + padSize()));
            this.recordSize = _tmp;
            return this.recordSize;
        }
        private Boolean alwaysUnique;
        public Boolean alwaysUnique() {
            if (this.alwaysUnique != null)
                return this.alwaysUnique;
            boolean _tmp = (boolean) ( ((fileIdLength() > 0) && ( ((cs0Type() == 254) || (cs0Type() == 255)) )) );
            this.alwaysUnique = _tmp;
            return this.alwaysUnique;
        }
        private Boolean isEmpty;
        public Boolean isEmpty() {
            if (this.isEmpty != null)
                return this.isEmpty;
            boolean _tmp = (boolean) ( ((!isUtf8()) && (!isUtf16())) );
            this.isEmpty = _tmp;
            return this.isEmpty;
        }
        private Boolean fileDeleted;
        public Boolean fileDeleted() {
            if (this.fileDeleted != null)
                return this.fileDeleted;
            boolean _tmp = (boolean) ((fileCharacteristics() & 4) != 0);
            this.fileDeleted = _tmp;
            return this.fileDeleted;
        }
        private DescriptorTag tag;
        private int fileVersionNum;
        private int fileCharacteristics;
        private int fileIdLength;
        private LongAd icbAd;
        private int implUseLength;
        private byte[] implUse;
        private Integer cs0Type;
        private String fileIdUtf8;
        private String fileIdUtf16;
        private byte[] fileIdEmpty;
        private byte[] padding;
        private Udf _root;
        private KaitaiStruct _parent;
        public DescriptorTag tag() { return tag; }
        public int fileVersionNum() { return fileVersionNum; }
        public int fileCharacteristics() { return fileCharacteristics; }
        public int fileIdLength() { return fileIdLength; }
        public LongAd icbAd() { return icbAd; }
        public int implUseLength() { return implUseLength; }
        public byte[] implUse() { return implUse; }
        public Integer cs0Type() { return cs0Type; }
        public String fileIdUtf8() { return fileIdUtf8; }
        public String fileIdUtf16() { return fileIdUtf16; }
        public byte[] fileIdEmpty() { return fileIdEmpty; }
        public byte[] padding() { return padding; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class LogicalVolumeDescBody extends KaitaiStruct {
        public static LogicalVolumeDescBody fromFile(String fileName) throws IOException {
            return new LogicalVolumeDescBody(new KaitaiStream(fileName));
        }

        public LogicalVolumeDescBody(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public LogicalVolumeDescBody(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public LogicalVolumeDescBody(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.descriptorCharSet = new Charspec(this._io, this, _root);
            this.logicalVolId = new Dstring128(this._io, this, _root);
            this.logicalBlockSize = this._io.readU4le();
            this.domainId = new EntityId(this._io, this, _root);
            this.fileSetDescExtent = new LongAd(this._io, this, _root);
            this.mapTableLength = this._io.readU4le();
            this.numPartitionMaps = this._io.readU4le();
            this.implementationId = new EntityId(this._io, this, _root);
            this.implementationUse = new String(this._io.readBytes(128), Charset.forName("UTF-8"));
            this.integritySeqExtent = this._io.readBytes(8);
            if (numPartitionMaps() > 0) {
                partitionMaps = new ArrayList<PartitionMap>((int) (numPartitionMaps()));
                for (int i = 0; i < numPartitionMaps(); i++) {
                    this.partitionMaps.add(new PartitionMap(this._io, this, _root));
                }
            }
        }
        private Charspec descriptorCharSet;
        private Dstring128 logicalVolId;
        private long logicalBlockSize;
        private EntityId domainId;
        private LongAd fileSetDescExtent;
        private long mapTableLength;
        private long numPartitionMaps;
        private EntityId implementationId;
        private String implementationUse;
        private byte[] integritySeqExtent;
        private ArrayList<PartitionMap> partitionMaps;
        private Udf _root;
        private KaitaiStruct _parent;
        public Charspec descriptorCharSet() { return descriptorCharSet; }
        public Dstring128 logicalVolId() { return logicalVolId; }
        public long logicalBlockSize() { return logicalBlockSize; }
        public EntityId domainId() { return domainId; }
        public LongAd fileSetDescExtent() { return fileSetDescExtent; }
        public long mapTableLength() { return mapTableLength; }
        public long numPartitionMaps() { return numPartitionMaps; }
        public EntityId implementationId() { return implementationId; }
        public String implementationUse() { return implementationUse; }
        public byte[] integritySeqExtent() { return integritySeqExtent; }
        public ArrayList<PartitionMap> partitionMaps() { return partitionMaps; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class AnchorVolDescPtr extends KaitaiStruct {
        public static AnchorVolDescPtr fromFile(String fileName) throws IOException {
            return new AnchorVolDescPtr(new KaitaiStream(fileName));
        }

        public AnchorVolDescPtr(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public AnchorVolDescPtr(KaitaiStream _io, Udf _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public AnchorVolDescPtr(KaitaiStream _io, Udf _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.tag = new AnchorPtrDescriptorTag(this._io, this, _root);
            this.mainVolDescSeqExtent = new AnchorPtrExtentAllocDesc(this._io, this, _root);
            this.reserveVolDesc = new AnchorPtrExtentAllocDesc(this._io, this, _root);
            this.reserved = this._io.readBytes(480);
        }
        private AnchorPtrDescriptorTag tag;
        private AnchorPtrExtentAllocDesc mainVolDescSeqExtent;
        private AnchorPtrExtentAllocDesc reserveVolDesc;
        private byte[] reserved;
        private Udf _root;
        private Udf _parent;
        public AnchorPtrDescriptorTag tag() { return tag; }
        public AnchorPtrExtentAllocDesc mainVolDescSeqExtent() { return mainVolDescSeqExtent; }
        public AnchorPtrExtentAllocDesc reserveVolDesc() { return reserveVolDesc; }
        public byte[] reserved() { return reserved; }
        public Udf _root() { return _root; }
        public Udf _parent() { return _parent; }
    }
    public static class IcbFileEntryBody extends KaitaiStruct {
        public static IcbFileEntryBody fromFile(String fileName) throws IOException {
            return new IcbFileEntryBody(new KaitaiStream(fileName));
        }

        public IcbFileEntryBody(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public IcbFileEntryBody(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public IcbFileEntryBody(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.uid = this._io.readU4le();
            this.gid = this._io.readU4le();
            this.permissions = this._io.readU4le();
            this.fileLinkCount = this._io.readU2le();
            this.recordFormat = this._io.ensureFixedContents(new byte[] { 0 });
            this.recordDisplayAttributes = this._io.ensureFixedContents(new byte[] { 0 });
            this.recordLength = this._io.ensureFixedContents(new byte[] { 0, 0, 0, 0 });
            this.informationLength = this._io.readU8le();
            this.logicalBlocksRecorded = this._io.readU8le();
            this.accessTime = new Timestamp(this._io, this, _root);
            this.modificationTime = new Timestamp(this._io, this, _root);
            this.attributeTime = new Timestamp(this._io, this, _root);
            this.checkpoint = this._io.readU4le();
            this.extendedAttributeIcb = new LongAd(this._io, this, _root);
            this.implementationId = new EntityId(this._io, this, _root);
            this.uniqueId = this._io.readU8le();
            this.lengthOfExtendedAttributes = this._io.readU4le();
            this.lengthOfAllocationDesc = this._io.readU4le();
            this.extendedAttributes = this._io.readBytes(lengthOfExtendedAttributes());
            this.allocationDescriptors = this._io.readBytes(lengthOfAllocationDesc());
        }
        private long uid;
        private long gid;
        private long permissions;
        private int fileLinkCount;
        private byte[] recordFormat;
        private byte[] recordDisplayAttributes;
        private byte[] recordLength;
        private long informationLength;
        private long logicalBlocksRecorded;
        private Timestamp accessTime;
        private Timestamp modificationTime;
        private Timestamp attributeTime;
        private long checkpoint;
        private LongAd extendedAttributeIcb;
        private EntityId implementationId;
        private long uniqueId;
        private long lengthOfExtendedAttributes;
        private long lengthOfAllocationDesc;
        private byte[] extendedAttributes;
        private byte[] allocationDescriptors;
        private Udf _root;
        private KaitaiStruct _parent;
        public long uid() { return uid; }
        public long gid() { return gid; }
        public long permissions() { return permissions; }
        public int fileLinkCount() { return fileLinkCount; }
        public byte[] recordFormat() { return recordFormat; }
        public byte[] recordDisplayAttributes() { return recordDisplayAttributes; }
        public byte[] recordLength() { return recordLength; }
        public long informationLength() { return informationLength; }
        public long logicalBlocksRecorded() { return logicalBlocksRecorded; }
        public Timestamp accessTime() { return accessTime; }
        public Timestamp modificationTime() { return modificationTime; }
        public Timestamp attributeTime() { return attributeTime; }
        public long checkpoint() { return checkpoint; }
        public LongAd extendedAttributeIcb() { return extendedAttributeIcb; }
        public EntityId implementationId() { return implementationId; }
        public long uniqueId() { return uniqueId; }
        public long lengthOfExtendedAttributes() { return lengthOfExtendedAttributes; }
        public long lengthOfAllocationDesc() { return lengthOfAllocationDesc; }
        public byte[] extendedAttributes() { return extendedAttributes; }
        public byte[] allocationDescriptors() { return allocationDescriptors; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class AnchorPtrExtentAllocDesc extends KaitaiStruct {
        public static AnchorPtrExtentAllocDesc fromFile(String fileName) throws IOException {
            return new AnchorPtrExtentAllocDesc(new KaitaiStream(fileName));
        }

        public AnchorPtrExtentAllocDesc(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public AnchorPtrExtentAllocDesc(KaitaiStream _io, AnchorVolDescPtr _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public AnchorPtrExtentAllocDesc(KaitaiStream _io, AnchorVolDescPtr _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.length = this._io.readU4le();
            this.location = this._io.readU4le();
        }
        private long length;
        private long location;
        private Udf _root;
        private Udf.AnchorVolDescPtr _parent;
        public long length() { return length; }
        public long location() { return location; }
        public Udf _root() { return _root; }
        public Udf.AnchorVolDescPtr _parent() { return _parent; }
    }
    public static class IcbHeader extends KaitaiStruct {
        public static IcbHeader fromFile(String fileName) throws IOException {
            return new IcbHeader(new KaitaiStream(fileName));
        }

        public IcbHeader(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public IcbHeader(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public IcbHeader(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.tag = new DescriptorTag(this._io, this, _root);
            this.icbTag = new Icbtag(this._io, this, _root);
        }
        private DescriptorTag tag;
        private Icbtag icbTag;
        private Udf _root;
        private KaitaiStruct _parent;
        public DescriptorTag tag() { return tag; }
        public Icbtag icbTag() { return icbTag; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class EntityId extends KaitaiStruct {
        public static EntityId fromFile(String fileName) throws IOException {
            return new EntityId(new KaitaiStream(fileName));
        }

        public EntityId(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public EntityId(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public EntityId(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.flags = this._io.readU1();
            this.itentifier = new String(this._io.readBytes(23), Charset.forName("UTF-8"));
            this.identifierSuffix = new String(this._io.readBytes(8), Charset.forName("UTF-8"));
        }
        private int flags;
        private String itentifier;
        private String identifierSuffix;
        private Udf _root;
        private KaitaiStruct _parent;
        public int flags() { return flags; }
        public String itentifier() { return itentifier; }
        public String identifierSuffix() { return identifierSuffix; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class IsoVolDesc extends KaitaiStruct {
        public static IsoVolDesc fromFile(String fileName) throws IOException {
            return new IsoVolDesc(new KaitaiStream(fileName));
        }

        public IsoVolDesc(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public IsoVolDesc(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public IsoVolDesc(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.type = this._io.readU1();
            this.magic = this._io.ensureFixedContents(new byte[] { 67, 68, 48, 48, 49 });
        }
        private int type;
        private byte[] magic;
        private Udf _root;
        private KaitaiStruct _parent;
        public int type() { return type; }
        public byte[] magic() { return magic; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class PartitionDescBody extends KaitaiStruct {
        public static PartitionDescBody fromFile(String fileName) throws IOException {
            return new PartitionDescBody(new KaitaiStream(fileName));
        }

        public PartitionDescBody(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public PartitionDescBody(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public PartitionDescBody(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.partitionFlags = this._io.readU2le();
            this.partitionNumber = this._io.readU2le();
            this.partitionContents = new EntityId(this._io, this, _root);
            this.partitionContentsUse = this._io.readBytes(128);
            this.accessType = this._io.readU4le();
            this.partitionStartingLocation = this._io.readU4le();
            this.partitionLength = this._io.readU4le();
            this.implementationId = new EntityId(this._io, this, _root);
            this.implementationUse = this._io.readBytes(128);
            this.reserved = this._io.readBytes(156);
        }
        private int partitionFlags;
        private int partitionNumber;
        private EntityId partitionContents;
        private byte[] partitionContentsUse;
        private long accessType;
        private long partitionStartingLocation;
        private long partitionLength;
        private EntityId implementationId;
        private byte[] implementationUse;
        private byte[] reserved;
        private Udf _root;
        private KaitaiStruct _parent;
        public int partitionFlags() { return partitionFlags; }
        public int partitionNumber() { return partitionNumber; }
        public EntityId partitionContents() { return partitionContents; }
        public byte[] partitionContentsUse() { return partitionContentsUse; }
        public long accessType() { return accessType; }
        public long partitionStartingLocation() { return partitionStartingLocation; }
        public long partitionLength() { return partitionLength; }
        public EntityId implementationId() { return implementationId; }
        public byte[] implementationUse() { return implementationUse; }
        public byte[] reserved() { return reserved; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class FileSetDescriptorTag extends KaitaiStruct {
        public static FileSetDescriptorTag fromFile(String fileName) throws IOException {
            return new FileSetDescriptorTag(new KaitaiStream(fileName));
        }

        public FileSetDescriptorTag(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public FileSetDescriptorTag(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public FileSetDescriptorTag(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.tagId = Udf.TagIdentifier.byId(this._io.readU2le());
            this.descVer = this._io.readU2le();
            this.tagCksum = this._io.readU1();
            this.reserved = this._io.readU1();
            this.tagSerialNum = this._io.readU2le();
            this.descCrc = this._io.readU2le();
            this.descSerialNumLength = this._io.readU2le();
            this.tagLocation = this._io.readU4le();
        }
        private TagIdentifier tagId;
        private int descVer;
        private int tagCksum;
        private int reserved;
        private int tagSerialNum;
        private int descCrc;
        private int descSerialNumLength;
        private long tagLocation;
        private Udf _root;
        private KaitaiStruct _parent;
        public TagIdentifier tagId() { return tagId; }
        public int descVer() { return descVer; }
        public int tagCksum() { return tagCksum; }
        public int reserved() { return reserved; }
        public int tagSerialNum() { return tagSerialNum; }
        public int descCrc() { return descCrc; }
        public int descSerialNumLength() { return descSerialNumLength; }
        public long tagLocation() { return tagLocation; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class UdfVolDesc2a extends KaitaiStruct {
        public static UdfVolDesc2a fromFile(String fileName) throws IOException {
            return new UdfVolDesc2a(new KaitaiStream(fileName));
        }

        public UdfVolDesc2a(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public UdfVolDesc2a(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public UdfVolDesc2a(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.type = this._io.readU1();
            this.magic = this._io.ensureFixedContents(new byte[] { 78, 83, 82, 48, 50 });
        }
        private int type;
        private byte[] magic;
        private Udf _root;
        private KaitaiStruct _parent;
        public int type() { return type; }
        public byte[] magic() { return magic; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class LongAd extends KaitaiStruct {
        public static LongAd fromFile(String fileName) throws IOException {
            return new LongAd(new KaitaiStream(fileName));
        }

        public LongAd(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public LongAd(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public LongAd(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.extentLenAndType = this._io.readU4le();
            this.extentLocation = new LbAddr(this._io, this, _root);
            this.implementationUse = this._io.readBytes(6);
        }
        private Integer extentLength;
        public Integer extentLength() {
            if (this.extentLength != null)
                return this.extentLength;
            int _tmp = (int) ((extentLenAndType() & 16383));
            this.extentLength = _tmp;
            return this.extentLength;
        }
        private ExtentClass extentType;
        public ExtentClass extentType() {
            if (this.extentType != null)
                return this.extentType;
            this.extentType = Udf.ExtentClass.byId((extentLenAndType() >> 30));
            return this.extentType;
        }
        private long extentLenAndType;
        private LbAddr extentLocation;
        private byte[] implementationUse;
        private Udf _root;
        private KaitaiStruct _parent;
        public long extentLenAndType() { return extentLenAndType; }
        public LbAddr extentLocation() { return extentLocation; }
        public byte[] implementationUse() { return implementationUse; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class VolumeDescHeader extends KaitaiStruct {
        public static VolumeDescHeader fromFile(String fileName) throws IOException {
            return new VolumeDescHeader(new KaitaiStream(fileName));
        }

        public VolumeDescHeader(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public VolumeDescHeader(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public VolumeDescHeader(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.tag = new DescriptorTag(this._io, this, _root);
            this.volDescSequenceNumber = this._io.readU4le();
        }
        private DescriptorTag tag;
        private long volDescSequenceNumber;
        private Udf _root;
        private KaitaiStruct _parent;
        public DescriptorTag tag() { return tag; }
        public long volDescSequenceNumber() { return volDescSequenceNumber; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class LbAddr extends KaitaiStruct {
        public static LbAddr fromFile(String fileName) throws IOException {
            return new LbAddr(new KaitaiStream(fileName));
        }

        public LbAddr(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public LbAddr(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public LbAddr(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.logicalBlockNum = this._io.readU4le();
            this.partitionRefNum = this._io.readU2le();
        }
        private long logicalBlockNum;
        private int partitionRefNum;
        private Udf _root;
        private KaitaiStruct _parent;
        public long logicalBlockNum() { return logicalBlockNum; }
        public int partitionRefNum() { return partitionRefNum; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class Dstring128 extends KaitaiStruct {
        public static Dstring128 fromFile(String fileName) throws IOException {
            return new Dstring128(new KaitaiStream(fileName));
        }

        public Dstring128(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public Dstring128(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public Dstring128(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.value = new String(this._io.readBytes(127), Charset.forName("UTF-8"));
            this.len = this._io.readU1();
        }
        private String value;
        private int len;
        private Udf _root;
        private KaitaiStruct _parent;
        public String value() { return value; }
        public int len() { return len; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class UdfVolDesc2b extends KaitaiStruct {
        public static UdfVolDesc2b fromFile(String fileName) throws IOException {
            return new UdfVolDesc2b(new KaitaiStream(fileName));
        }

        public UdfVolDesc2b(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public UdfVolDesc2b(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public UdfVolDesc2b(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.type = this._io.readU1();
            this.magic = this._io.ensureFixedContents(new byte[] { 78, 83, 82, 48, 51 });
        }
        private int type;
        private byte[] magic;
        private Udf _root;
        private KaitaiStruct _parent;
        public int type() { return type; }
        public byte[] magic() { return magic; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class Dstring32 extends KaitaiStruct {
        public static Dstring32 fromFile(String fileName) throws IOException {
            return new Dstring32(new KaitaiStream(fileName));
        }

        public Dstring32(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public Dstring32(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public Dstring32(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.value = new String(this._io.readBytes(31), Charset.forName("UTF-8"));
            this.len = this._io.readU1();
        }
        private String value;
        private int len;
        private Udf _root;
        private KaitaiStruct _parent;
        public String value() { return value; }
        public int len() { return len; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class UdfVolDesc1 extends KaitaiStruct {
        public static UdfVolDesc1 fromFile(String fileName) throws IOException {
            return new UdfVolDesc1(new KaitaiStream(fileName));
        }

        public UdfVolDesc1(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public UdfVolDesc1(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public UdfVolDesc1(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.type = this._io.readU1();
            this.magic = this._io.ensureFixedContents(new byte[] { 66, 69, 65, 48, 49 });
        }
        private int type;
        private byte[] magic;
        private Udf _root;
        private KaitaiStruct _parent;
        public int type() { return type; }
        public byte[] magic() { return magic; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class Charspec extends KaitaiStruct {
        public static Charspec fromFile(String fileName) throws IOException {
            return new Charspec(new KaitaiStream(fileName));
        }

        public Charspec(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public Charspec(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public Charspec(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.charSetType = this._io.ensureFixedContents(new byte[] { 0 });
            this.charSetInfo = this._io.ensureFixedContents(new byte[] { 79, 83, 84, 65, 32, 67, 111, 109, 112, 114, 101, 115, 115, 101, 100, 32, 85, 110, 105, 99, 111, 100, 101, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        }
        private byte[] charSetType;
        private byte[] charSetInfo;
        private Udf _root;
        private KaitaiStruct _parent;
        public byte[] charSetType() { return charSetType; }
        public byte[] charSetInfo() { return charSetInfo; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    public static class AnchorPtrDescriptorTag extends KaitaiStruct {
        public static AnchorPtrDescriptorTag fromFile(String fileName) throws IOException {
            return new AnchorPtrDescriptorTag(new KaitaiStream(fileName));
        }

        public AnchorPtrDescriptorTag(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public AnchorPtrDescriptorTag(KaitaiStream _io, AnchorVolDescPtr _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public AnchorPtrDescriptorTag(KaitaiStream _io, AnchorVolDescPtr _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.tagId = Udf.TagIdentifier.byId(this._io.readU2le());
            this.descVer = this._io.readU2le();
            this.tagCksum = this._io.readU1();
            this.reserved = this._io.readU1();
            this.tagSerialNum = this._io.readU2le();
            this.descCrc = this._io.readU2le();
            this.descSerialNumLength = this._io.readU2le();
            this.tagLocation = this._io.readU4le();
        }
        private TagIdentifier tagId;
        private int descVer;
        private int tagCksum;
        private int reserved;
        private int tagSerialNum;
        private int descCrc;
        private int descSerialNumLength;
        private long tagLocation;
        private Udf _root;
        private Udf.AnchorVolDescPtr _parent;
        public TagIdentifier tagId() { return tagId; }
        public int descVer() { return descVer; }
        public int tagCksum() { return tagCksum; }
        public int reserved() { return reserved; }
        public int tagSerialNum() { return tagSerialNum; }
        public int descCrc() { return descCrc; }
        public int descSerialNumLength() { return descSerialNumLength; }
        public long tagLocation() { return tagLocation; }
        public Udf _root() { return _root; }
        public Udf.AnchorVolDescPtr _parent() { return _parent; }
    }
    public static class ShortAd extends KaitaiStruct {
        public static ShortAd fromFile(String fileName) throws IOException {
            return new ShortAd(new KaitaiStream(fileName));
        }

        public ShortAd(KaitaiStream _io) {
            super(_io);
            _read();
        }

        public ShortAd(KaitaiStream _io, KaitaiStruct _parent) {
            super(_io);
            this._parent = _parent;
            _read();
        }

        public ShortAd(KaitaiStream _io, KaitaiStruct _parent, Udf _root) {
            super(_io);
            this._parent = _parent;
            this._root = _root;
            _read();
        }
        private void _read() {
            this.extentLenAndType = this._io.readU4le();
            this.extentBlock = this._io.readU4le();
        }
        private Integer extentLength;
        public Integer extentLength() {
            if (this.extentLength != null)
                return this.extentLength;
            int _tmp = (int) ((extentLenAndType() & 1073741823));
            this.extentLength = _tmp;
            return this.extentLength;
        }
        private ExtentClass extentType;
        public ExtentClass extentType() {
            if (this.extentType != null)
                return this.extentType;
            this.extentType = Udf.ExtentClass.byId((extentLenAndType() >> 30));
            return this.extentType;
        }
        private long extentLenAndType;
        private long extentBlock;
        private Udf _root;
        private KaitaiStruct _parent;
        public long extentLenAndType() { return extentLenAndType; }
        public long extentBlock() { return extentBlock; }
        public Udf _root() { return _root; }
        public KaitaiStruct _parent() { return _parent; }
    }
    private Integer sectorSize;
    public Integer sectorSize() {
        if (this.sectorSize != null)
            return this.sectorSize;
        int _tmp = (int) (2048);
        this.sectorSize = _tmp;
        return this.sectorSize;
    }
    private AnchorVolDescPtr anchorVolDescPtrForVolume;
    public AnchorVolDescPtr anchorVolDescPtrForVolume() {
        if (this.anchorVolDescPtrForVolume != null)
            return this.anchorVolDescPtrForVolume;
        long _pos = this._io.pos();
        this._io.seek((sectorSize() * 256));
        this.anchorVolDescPtrForVolume = new AnchorVolDescPtr(this._io, this, _root);
        this._io.seek(_pos);
        return this.anchorVolDescPtrForVolume;
    }
    private Udf _root;
    private KaitaiStruct _parent;
    public Udf _root() { return _root; }
    public KaitaiStruct _parent() { return _parent; }
}
