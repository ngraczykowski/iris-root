package com.silenteight.agent.common.messaging;

import lombok.RequiredArgsConstructor;

import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FrameInputStream;
import org.springframework.amqp.support.postprocessor.AbstractDecompressingPostProcessor;

import java.io.IOException;
import java.io.InputStream;

import static com.silenteight.agent.common.messaging.AgentMessagePostProcessors.LZ4_ENCODING;

@RequiredArgsConstructor
class Lz4DecompressingPostProcessor extends AbstractDecompressingPostProcessor {

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

  @Override
  public int getOrder() {
    return 2;
  }
}
