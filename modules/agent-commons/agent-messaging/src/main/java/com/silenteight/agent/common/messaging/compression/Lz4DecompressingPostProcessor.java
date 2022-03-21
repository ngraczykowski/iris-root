package com.silenteight.agent.common.messaging.compression;

import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FrameInputStream;
import org.springframework.amqp.support.postprocessor.AbstractDecompressingPostProcessor;

import java.io.IOException;
import java.io.InputStream;

public class Lz4DecompressingPostProcessor extends AbstractDecompressingPostProcessor {

  private static final String LZ4_ENCODING = "lz4";

  @Override
  protected InputStream getDecompressorStream(InputStream stream) throws IOException {
    return new LZ4FrameInputStream(
        stream,
        LZ4Factory.fastestInstance().safeDecompressor(),
        Lz4Utils.createHash());
  }

  @Override
  protected String getEncoding() {
    return LZ4_ENCODING;
  }
}
