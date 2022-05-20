package com.silenteight.sep.base.common.messaging.compression;

import lombok.Value;

@Value
class SimpleCompressionBundle implements CompressionBundle {

  Compressor compressor;
  Decompressor decompressor;
  String encoding;
}
