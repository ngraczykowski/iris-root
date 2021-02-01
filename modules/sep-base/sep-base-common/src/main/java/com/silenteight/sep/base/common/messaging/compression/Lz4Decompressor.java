package com.silenteight.sep.base.common.messaging.compression;

import com.silenteight.sep.base.common.messaging.compression.CompressionBundle.Decompressor;

import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FrameInputStream;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

class Lz4Decompressor implements Decompressor {

  @Override
  public @NotNull InputStream decompress(@NotNull InputStream stream) throws IOException {
    return new LZ4FrameInputStream(
        stream,
        LZ4Factory.fastestInstance().safeDecompressor(),
        Lz4Utils.createHash());
  }
}
