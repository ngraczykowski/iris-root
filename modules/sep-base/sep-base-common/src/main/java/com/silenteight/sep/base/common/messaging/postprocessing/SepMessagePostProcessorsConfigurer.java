package com.silenteight.sep.base.common.messaging.postprocessing;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.sep.base.common.messaging.compression.CompressionBundle;
import com.silenteight.sep.base.common.messaging.encryption.AmqpMessageDecrypter;
import com.silenteight.sep.base.common.messaging.encryption.AmqpMessageEncypter;
import com.silenteight.sep.base.common.support.messaging.CompositeAmqpMessagesPostProcessor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.support.postprocessor.DelegatingDecompressingPostProcessor;
import org.springframework.amqp.support.postprocessor.GUnzipPostProcessor;
import org.springframework.amqp.support.postprocessor.UnzipPostProcessor;

import java.util.ArrayList;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class SepMessagePostProcessorsConfigurer {

  @Setter
  @Nullable
  private AmqpMessageDecrypter messageDecrypter;

  @Setter
  @Nullable
  private AmqpMessageEncypter messageEncrypter;

  @Setter
  @Nullable
  private CompressionBundle compressionBundle;

  @NotNull
  public SepMessagePostProcessors configure() {
    return new SimpleSepMessagePostProcessors(
        createSendPostProcessor(),
        createReceivePostProcessor());
  }

  private MessagePostProcessor createSendPostProcessor() {
    var sendPostProcessors = new ArrayList<MessagePostProcessor>();

    ofNullable(compressionBundle)
        .map(CompressMessagePostProcessor::fromBundle)
        .ifPresent(sendPostProcessors::add);

    ofNullable(messageEncrypter)
        .map(EncryptMessagePostProcessor::new)
        .ifPresent(sendPostProcessors::add);

    return new CompositeAmqpMessagesPostProcessor(sendPostProcessors);
  }

  private MessagePostProcessor createReceivePostProcessor() {
    var receivePostProcessors = new ArrayList<MessagePostProcessor>();

    receivePostProcessors.add(createDecompressingPostProcessor());

    ofNullable(messageDecrypter)
        .map(DecryptMessagePostProcessor::new)
        .ifPresent(receivePostProcessors::add);

    return new CompositeAmqpMessagesPostProcessor(receivePostProcessors);
  }

  private MessagePostProcessor createDecompressingPostProcessor() {
    var postProcessor =
        new DelegatingDecompressingPostProcessor();

    ofNullable(compressionBundle)
        .map(DecompressMessagePostProcessor::fromBundle)
        .ifPresent(decompressingProcessor ->
            postProcessor.addDecompressor(
                decompressingProcessor.getEncoding(), decompressingProcessor));

    postProcessor.addDecompressor("zip", new UnzipPostProcessor());
    postProcessor.addDecompressor("gzip", new GUnzipPostProcessor());

    return postProcessor;
  }
}
