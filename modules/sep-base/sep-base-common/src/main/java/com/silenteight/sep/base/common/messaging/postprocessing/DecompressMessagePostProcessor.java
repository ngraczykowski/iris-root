package com.silenteight.sep.base.common.messaging.postprocessing;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.compression.CompressionBundle;
import com.silenteight.sep.base.common.messaging.compression.CompressionBundle.Decompressor;

import org.springframework.amqp.support.postprocessor.AbstractDecompressingPostProcessor;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
class DecompressMessagePostProcessor extends AbstractDecompressingPostProcessor {

  private final Decompressor decompressor;
  private final String encoding;

  static DecompressMessagePostProcessor fromBundle(CompressionBundle bundle) {
    return new DecompressMessagePostProcessor(bundle.getDecompressor(), bundle.getEncoding());
  }

  @Override
  protected InputStream getDecompressorStream(InputStream stream) throws IOException {
    return decompressor.decompress(stream);
  }

  @Override
  public int getOrder() {
    return MessageProcessorsOrdering.DECOMPRESSION;
  }

  @Override
  protected String getEncoding() {
    return encoding;
  }
}
