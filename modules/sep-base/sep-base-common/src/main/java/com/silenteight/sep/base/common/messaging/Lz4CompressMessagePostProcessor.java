package com.silenteight.sep.base.common.messaging;

import lombok.Getter;
import lombok.Setter;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FrameOutputStream;
import net.jpountz.lz4.LZ4FrameOutputStream.BLOCKSIZE;
import net.jpountz.lz4.LZ4FrameOutputStream.FLG.Bits;
import org.springframework.amqp.support.postprocessor.AbstractCompressingPostProcessor;

import java.io.IOException;
import java.io.OutputStream;

@Getter
@Setter
public class Lz4CompressMessagePostProcessor extends AbstractCompressingPostProcessor {

  public static final int FASTEST_COMPRESSION = 1;

  public static final int DEFAULT_COMPRESSION = FASTEST_COMPRESSION;

  public static final int BEST_COMPRESSION = 9;

  private static final LZ4Factory FACTORY = LZ4Factory.fastestInstance();

  private int compressionLevel = 1;

  @Override
  protected OutputStream getCompressorStream(OutputStream stream) throws IOException {
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

  @Override
  public int getOrder() {
    return MessageProcessorsOrdering.COMPRESSION;
  }

  @Override
  protected String getEncoding() {
    return "lz4";
  }
}
