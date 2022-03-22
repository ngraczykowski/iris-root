package com.silenteight.agent.common.messaging;

import lombok.RequiredArgsConstructor;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FrameOutputStream;
import net.jpountz.lz4.LZ4FrameOutputStream.BLOCKSIZE;
import net.jpountz.lz4.LZ4FrameOutputStream.FLG.Bits;
import org.springframework.amqp.support.postprocessor.AbstractCompressingPostProcessor;

import java.io.IOException;
import java.io.OutputStream;

import static com.silenteight.agent.common.messaging.AgentMessagePostProcessors.LZ4_ENCODING;

@RequiredArgsConstructor
class Lz4CompressingPostProcessor extends AbstractCompressingPostProcessor {

  private static final LZ4Factory FACTORY = LZ4Factory.fastestInstance();

  private final int compressionLevel;

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
  protected String getEncoding() {
    return LZ4_ENCODING;
  }

  @Override
  public int getOrder() {
    return 1;
  }
}
