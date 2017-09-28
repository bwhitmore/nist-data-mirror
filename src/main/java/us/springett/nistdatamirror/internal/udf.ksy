# This file is Copyright (c) 2017 Brent Whitmore. All Rights Reserved.
# 
# This file is part of nist-data-mirror.
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
# http://www.apache.org/licenses/LICENSE-2.0
#  
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

meta:
  id: udf
  file-extension: [iso, udf]
  endian: le
  encoding: UTF-8
instances:
  sector_size:
    value: 2048
  anchor_vol_desc_ptr_for_volume:
    type: anchor_vol_desc_ptr
    pos: sector_size * 256
enums:
  tag_identifier:
    0x0001:  primary_volume_descriptor
    0x0002:  anchor_volume_descriptor_pointer
    0x0003:  volume_descriptor_pointer
    0x0004:  implementation_use_volume_descriptor
    0x0005:  partition_descriptor
    0x0006:  logical_volume_descriptor
    0x0007:  unallocated_space_descriptor
    0x0008:  terminating_descriptor
    0x0009:  logical_volume_integrity_descriptor
    0x0100:  file_set_descriptor
    0x0101:  file_identifier_descriptor
    0x0102:  allocation_extent_descriptor
    0x0103:  indirect_entry
    0x0104:  terminal_entry
    0x0105:  file_entry
    0x0106:  extended_attribute_header_descriptor
    0x0107:  unallocated_space_entry
    0x0108:  space_bitmap_descriptor
    0x0109:  partition_integrity_entry
    0x010a:  extended_file_entry
  extent_class:
    0x0: extent_recorded_allocated
    0x1: extent_allocated_but_not_recorded
    0x2: extent_neither_allocated_nor_recorded
    0x3: next_extent_of_allocation_descriptors
  icb_file_type:
    0: unspecified
    1: unallocated_space_entry
    2: partition_integrity_entry
    3: indirect_entry
    4: directory
    5: byte_stream
    6: block_special_device
    7: character_special_device
    8: extended_attributes
    9: pipe
    10: socket
    11: terminal
    12: symbolic_link
    13: stream_directory
    248: agreement_248
    249: agreement_249
    250: agreement_250
    251: agreement_251
    252: agreement_252
    253: agreement_253
    254: agreement_254
    255: agreement_255
  icb_desc_use:
    0: use_short_ad
    1: use_long_ad
    2: use_extended_ad
    3: immediate
types:
  dstring32:
    seq:
      - id: value
        type: str
        size: 31
      - id: len
        type: u1
  dstring128:
    seq:
      - id: value
        type: str
        size: 127
      - id: len
        type: u1
  charspec:
    seq:
      - id: char_set_type
        contents: "\0"
      - id: char_set_info
        contents: [ 0x4f, 0x53, 0x54, 0x41, 0x20, 0x43, 0x6f, 0x6d, 0x70, 0x72, 0x65, 0x73, 0x73, 0x65, 0x64, 0x20, 
                    0x55, 0x6e, 0x69, 0x63, 0x6f, 0x64, 0x65, 0,    0,    0,    0,    0,    0,    0,    0,    0,
                    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
                    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0  ]
  entity_id:
    seq:
      - id: flags
        type: u1
      - id: itentifier
        type: str
        size: 23
      - id: identifier_suffix
        type: str
        size: 8
  lb_addr:
    seq:
      - id: logical_block_num
        type: u4
      - id: partition_ref_num
        type: u2
  long_ad:
    seq:
      - id: extent_len_and_type
        type: u4
      - id: extent_location
        type: lb_addr
      - id: implementation_use
        size: 6
    instances:
        extent_length:
          value: extent_len_and_type & 0x3fff 
        extent_type:
          value: extent_len_and_type >> 30
          enum: extent_class
  short_ad:
    seq:
      - id: extent_len_and_type
        type: u4
      - id: extent_block
        type: u4
    instances:
        extent_length:
          value: extent_len_and_type & 0x3fffffff
        extent_type:
          value: extent_len_and_type >> 30
          enum: extent_class
  timestamp:
    seq:
      - id: type_and_tz
        type: u2
      - id: year
        type: s2
      - id: month
        type: u1
      - id: day
        type: u1
      - id: hour
        type: u1
      - id: minute
        type: u1
      - id: second
        type: u1
      - id: centiseconds
        type: u1
      - id: hundreds_of_microseconds
        type: u1
      - id: microseconds
        type: u1
  iso_vol_desc:
    seq:
      - id: type
        type: u1
      - id: magic
        contents: "CD001"
  udf_vol_desc_1:
    seq:
      - id: type
        type: u1
      - id: magic
        contents: "BEA01"
  udf_vol_desc_2a:
    seq:
      - id: type
        type: u1
      - id: magic
        contents: "NSR02"
  udf_vol_desc_2b:
    seq:
      - id: type
        type: u1
      - id: magic
        contents: "NSR03"
  udf_vol_desc_3:
    seq:
      - id: type
        type: u1
      - id: magic
        contents: "TEA01"
  descriptor_tag:
    seq:
      - id: tag_id
        type: u2
        enum: tag_identifier
      - id: desc_ver
        type: u2
      - id: tag_cksum
        type: u1
      - id: reserved
        type: u1
      - id: tag_serial_num
        type: u2
      - id: desc_crc
        type: u2
      - id: desc_serial_num_length
        type: u2
      - id: tag_location
        type: u4
  anchor_ptr_descriptor_tag:
    seq:
      - id: tag_id
        type: u2
        enum: tag_identifier
      - id: desc_ver
        type: u2
      - id: tag_cksum
        type: u1
      - id: reserved
        type: u1
      - id: tag_serial_num
        type: u2
      - id: desc_crc
        type: u2
      - id: desc_serial_num_length
        type: u2
      - id: tag_location
        type: u4
  file_set_descriptor_tag:
    seq:
      - id: tag_id
        type: u2
        enum: tag_identifier
      - id: desc_ver
        type: u2
      - id: tag_cksum
        type: u1
      - id: reserved
        type: u1
      - id: tag_serial_num
        type: u2
      - id: desc_crc
        type: u2
      - id: desc_serial_num_length
        type: u2
      - id: tag_location
        type: u4
  icbtag:
    seq:
      - id: prior_recorded_num_of_direct_entries
        type: u4
      - id: strategy_type
        type: u2
      - id: strategy_parameter
        size: 2
      - id: max_num_of_entries
        type: u2
      - id: reserved_1
        size: 1
      - id: file_type
        type: u1
        enum: icb_file_type
      - id: parent_icb_location
        type: lb_addr
      - id: flags
        type: u2
    instances:
      descriptor_use:
        value: flags & 0x7
        enum: icb_desc_use
      is_dir:
        value: (flags & 0x0008) != 0
      do_not_relocate:
        value: (flags & 0x0010) != 0
      archive:
        value: (flags & 0x0020) != 0
      setuid:
        value: (flags & 0x0040) != 0
      setgid:
        value: (flags & 0x0080) != 0
      sticky:
        value: (flags & 0x0100) != 0
      contiguous:
        value: (flags & 0x0200) != 0
      system:
        value: (flags & 0x0400) != 0
      transformed:
        value: (flags & 0x0800) != 0
      multiversions:
        value: (flags & 0x1000) != 0
      is_stream:
        value: (flags & 0x2000) != 0
  anchor_vol_desc_ptr:
    seq:
      - id: tag
        type: anchor_ptr_descriptor_tag
      - id: main_vol_desc_seq_extent
        type: anchor_ptr_extent_alloc_desc
      - id: reserve_vol_desc
        type: anchor_ptr_extent_alloc_desc
      - id: reserved
        size: 480
  anchor_ptr_extent_alloc_desc:
    seq:
      - id: length
        type: u4
      - id: location
        type: u4
  file_identifier_descriptor:
    seq:
      - id: tag
        type: descriptor_tag
      - id: file_version_num
        type: u2
      - id: file_characteristics
        type: u1
      - id: file_id_length
        type: u1
      - id: icb_ad
        type: long_ad
      - id: impl_use_length
        type: u2
      - id: impl_use
        size: impl_use_length
      - id: cs0_type
        if: file_id_length > 0
        type: u1
      - id: file_id_utf8
        if: is_utf8
        type: str
        size: file_id_length - 1
        encoding: UTF-8
      - id: file_id_utf16
        if: is_utf16
        type: str
        size: file_id_length - 1
        encoding: UTF-16
      - id: file_id_empty
        if: is_empty
        size: 0
      - id: padding
        size: pad_size
    instances:
        unpadded_record_size:
          value: impl_use_length + file_id_length + 38
        pad_size: 
          value: (4 - (unpadded_record_size % 4)) % 4
        record_size:
          value: unpadded_record_size + pad_size
        always_unique:
          value: (file_id_length > 0) and (cs0_type == 254 or cs0_type == 255)
        is_utf8:
          value: (file_id_length > 0) and (cs0_type == 8 or cs0_type == 254)
        is_utf16:
          value: (file_id_length > 0) and (cs0_type == 16 or cs0_type == 255)
        is_empty:
          value: not is_utf8 and not is_utf16
        is_hidden:
          value: (file_characteristics & 0x01) != 0
        is_directory:
          value: (file_characteristics & 0x02) != 0
        file_deleted:
          value: (file_characteristics & 0x04) != 0
        file_is_parent_dir:
          value: (file_characteristics & 0x08) != 0
        file_stream_is_meta:
          value: (file_characteristics & 0x10) != 0
  partition_desc_body:
    seq:
      - id: partition_flags
        type: u2
      - id: partition_number
        type: u2
      - id: partition_contents
        type: entity_id
      - id: partition_contents_use
        size: 128
      - id: access_type
        type: u4
      - id: partition_starting_location
        type: u4
      - id: partition_length # in sectors
        type: u4
      - id: implementation_id
        type: entity_id
      - id: implementation_use
        size: 128
      - id: reserved
        size: 156
  logical_volume_desc_body:
    seq:
      - id: descriptor_char_set
        type: charspec
      - id: logical_vol_id
        type: dstring128
      - id: logical_block_size
        type: u4
      - id: domain_id
        type: entity_id
      # - id: logical_volume_contents_use
      #   size: 16
      # ^ is overlayed by:
      - id: file_set_desc_extent        # points to file set descriptor
        type: long_ad
      - id: map_table_length
        type: u4
      - id: num_partition_maps
        type: u4
      - id: implementation_id
        type: entity_id
      - id: implementation_use          # reserved for content-defined use
        type: str
        size: 128
      - id: integrity_seq_extent        # should be subtype
        size: 8
      - id: partition_maps
        type: partition_map
        repeat: expr
        repeat-expr: num_partition_maps
        if: num_partition_maps > 0
  file_set_descriptor:
    seq:
      - id: tag
        type: file_set_descriptor_tag
      - id: recording_date_and_time
        type: timestamp
      - id: interchange_level
        type: u2
      - id: max_interchange_level
        type: u2
      - id: character_set_list
        type: u4
      - id: max_character_set_list
        type: u4
      - id: file_set_num
        type: u4
      - id: file_set_desc_num
        type: u4
      - id: logical_volume_id_char_set
        type: charspec
      - id: logical_volume_id
        type: dstring128
      - id: file_set_char_set
        type: charspec
      - id: file_set_id
        type: dstring32
      - id: copyright_file_id
        type: dstring32
      - id: abstract_file_id
        type: dstring32
      - id: root_directory_icb
        type: long_ad
      - id: domain_id
        type: entity_id
      - id: next_extent
        type: long_ad
      - id: system_stream_directory_icb
        type: long_ad
      - id: reserved
        size: 32
  volume_desc_header:
    seq:
      - id: tag
        type: descriptor_tag
      - id: vol_desc_sequence_number
        type: u4
  partition_map:
    seq:
      - id: pm_type
        type: u1
      - id: pm_length
        type: u8
      - id: reserved_1
        size: 2
      - id: partition_type_id
        type: entity_id
      - id: volume_seq_num
        type: u2
      - id: partition_number
        type: u2
      - id: map_record
        size: 24
  icb_header:
    seq:
      - id: tag
        type: descriptor_tag
      - id: icb_tag
        type: icbtag
  icb_file_entry_body:
    seq:
      - id: uid
        type: u4
      - id: gid
        type: u4
      - id: permissions
        type: u4
      - id: file_link_count
        type: u2
      - id: record_format
        contents: [0]
        # type: u1
      - id: record_display_attributes
        contents: [0]
        # type: u1
      - id: record_length
        contents: [0, 0, 0, 0]
        # type: u4
      - id: information_length
        type: u8
      - id: logical_blocks_recorded
        type: u8
      - id: access_time
        type: timestamp
      - id: modification_time
        type: timestamp
      - id: attribute_time
        type: timestamp
      - id: checkpoint
        type: u4
      - id: extended_attribute_icb
        type: long_ad
      - id: implementation_id
        type: entity_id
      - id: unique_id
        type: u8
      - id: length_of_extended_attributes
        type: u4
      - id: length_of_allocation_desc
        type: u4
      - id: extended_attributes
        size: length_of_extended_attributes
      - id: allocation_descriptors
        size: length_of_allocation_desc
