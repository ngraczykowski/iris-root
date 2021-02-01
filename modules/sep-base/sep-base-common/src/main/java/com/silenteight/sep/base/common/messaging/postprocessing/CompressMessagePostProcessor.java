package com.silenteight.sep.base.common.messaging.postprocessing;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.compression.CompressionBundle;
import com.silenteight.sep.base.common.messaging.compression.CompressionBundle.Compressor;

import org.springframework.amqp.support.postprocessor.AbstractCompressingPostProcessor;

import java.io.IOException;
import java.io.OutputStream;

@RequiredArgsConstructor
class CompressMessagePostProcessor extends AbstractCompressingPostProcessor {

  private final Compressor compressor;
  private final String encoding;

  static CompressMessagePostProcessor fromBundle(CompressionBundle bundle) {
    return new CompressMessagePostProcessor(bundle.getCompressor(), bundle.getEncoding());
  }

  @Override
  protected OutputStream getCompressorStream(OutputStream stream) throws IOException {
    return compressor.compress(stream);
  }

  @Override
  public int getOrder() {
    return MessageProcessorsOrdering.COMPRESSION;
  }

  @Override
  protected String getEncoding() {
    return encoding;
  }
}
