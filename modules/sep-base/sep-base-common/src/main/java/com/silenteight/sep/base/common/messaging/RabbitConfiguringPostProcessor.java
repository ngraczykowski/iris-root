package com.silenteight.sep.base.common.messaging;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.postprocessor.DelegatingDecompressingPostProcessor;
import org.springframework.amqp.support.postprocessor.GUnzipPostProcessor;
import org.springframework.amqp.support.postprocessor.UnzipPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ErrorHandler;

import java.util.Collection;

@RequiredArgsConstructor
class RabbitConfiguringPostProcessor implements BeanPostProcessor {

  private static final DelegatingDecompressingPostProcessor DECOMPRESSING_POST_PROCESSOR;

  private final MessagingProperties properties;
  private final Collection<ReceiveMessageListener> listeners;

  @Setter
  private ErrorHandler errorHandler;

  static {
    DelegatingDecompressingPostProcessor postProcessor =
        new DelegatingDecompressingPostProcessor();

    postProcessor.addDecompressor("lz4", new Lz4DecompressMessagePostProcessor());
    postProcessor.addDecompressor("zip", new UnzipPostProcessor());
    postProcessor.addDecompressor("gzip", new GUnzipPostProcessor());

    DECOMPRESSING_POST_PROCESSOR = postProcessor;
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) {

    if (bean instanceof RabbitTemplate)
      configureRabbitTemplate((RabbitTemplate) bean);

    if (AbstractRabbitListenerContainerFactory.class.isAssignableFrom(bean.getClass()))
      configureContainerFactory((AbstractRabbitListenerContainerFactory<?>) bean);

    return bean;
  }

  private void configureRabbitTemplate(RabbitTemplate template) {
    if (errorHandler != null)
      template.setReplyErrorHandler(errorHandler);

    if (properties.getCompression().isEnabled()) {
      Lz4CompressMessagePostProcessor compressor = new Lz4CompressMessagePostProcessor();
      compressor.setCompressionLevel(properties.getCompression().getLevel());

      template.addBeforePublishPostProcessors(compressor);
    }

    template.addAfterReceivePostProcessors(DECOMPRESSING_POST_PROCESSOR);
  }

  private void configureContainerFactory(AbstractRabbitListenerContainerFactory<?> factory) {
    if (errorHandler != null)
      factory.setErrorHandler(errorHandler);

    if (CollectionUtils.isEmpty(listeners)) {
      factory.setAfterReceivePostProcessors(DECOMPRESSING_POST_PROCESSOR);
      return;
    }

    ListeningMessagePostProcessor meteringProcessor =
        new ListeningMessagePostProcessor(listeners);

    factory.setAfterReceivePostProcessors(DECOMPRESSING_POST_PROCESSOR, meteringProcessor);
  }
}
