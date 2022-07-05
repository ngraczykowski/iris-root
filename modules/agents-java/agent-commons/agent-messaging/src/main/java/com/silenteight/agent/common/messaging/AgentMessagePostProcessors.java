package com.silenteight.agent.common.messaging;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.support.postprocessor.DelegatingDecompressingPostProcessor;
import org.springframework.amqp.support.postprocessor.GUnzipPostProcessor;
import org.springframework.amqp.support.postprocessor.UnzipPostProcessor;

@RequiredArgsConstructor
class AgentMessagePostProcessors {

  static final String LZ4_ENCODING = "lz4";

  private static final int FASTEST_COMPRESSION = 1;

  MessagePostProcessor getReceivePostProcessor() {
    var postProcessor = new DelegatingDecompressingPostProcessor();

    postProcessor.addDecompressor(LZ4_ENCODING, new Lz4DecompressingPostProcessor());
    postProcessor.addDecompressor("zip", new UnzipPostProcessor());
    postProcessor.addDecompressor("gzip", new GUnzipPostProcessor());

    return postProcessor;
  }

  MessagePostProcessor getPublishPostProcessor() {
    return new Lz4CompressingPostProcessor(FASTEST_COMPRESSION);
  }
}
