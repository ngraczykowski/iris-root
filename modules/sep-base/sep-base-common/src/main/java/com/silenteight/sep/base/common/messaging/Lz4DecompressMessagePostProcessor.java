package com.silenteight.sep.base.common.messaging;

import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FrameInputStream;
import org.springframework.amqp.support.postprocessor.AbstractDecompressingPostProcessor;

import java.io.IOException;
import java.io.InputStream;

public class Lz4DecompressMessagePostProcessor extends AbstractDecompressingPostProcessor {

  @Override
  protected InputStream getDecompressorStream(InputStream stream) throws IOException {
    return new LZ4FrameInputStream(
        stream,
        LZ4Factory.fastestInstance().safeDecompressor(),
        Lz4Utils.createHash());
  }

  @Override
  protected String getEncoding() {
    return "lz4";
  }
}
