package com.silenteight.sep.base.common.messaging.compression;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.compression.CompressionBundle.Compressor;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FrameOutputStream;
import net.jpountz.lz4.LZ4FrameOutputStream.BLOCKSIZE;
import net.jpountz.lz4.LZ4FrameOutputStream.FLG.Bits;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;

@RequiredArgsConstructor
class Lz4Compressor implements Compressor {

  private static final LZ4Factory FACTORY = LZ4Factory.fastestInstance();

  private final int compressionLevel;

  @Override
  public @NotNull OutputStream compress(@NotNull OutputStream stream) throws IOException {
    return new LZ4FrameOutputStream(
        stream,
        BLOCKSIZE.SIZE_64KB,
        -1,
        createCompressor(),
        Lz4Utils.createHash(),
        Bits.BLOCK_INDEPENDENCE);
  }

  private LZ4Compressor createCompressor() {
    if (compressionLevel <= 1)
      return FACTORY.fastCompressor();
    else
      return FACTORY.highCompressor(Math.min(compressionLevel, 9));
  }
}
