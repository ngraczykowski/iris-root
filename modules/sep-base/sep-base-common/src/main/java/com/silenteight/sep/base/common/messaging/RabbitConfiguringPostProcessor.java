package com.silenteight.sep.base.common.messaging;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.sep.base.common.messaging.encryption.AmqpMessageDecrypter;
import com.silenteight.sep.base.common.messaging.encryption.AmqpMessageEncypter;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.postprocessor.DelegatingDecompressingPostProcessor;
import org.springframework.amqp.support.postprocessor.GUnzipPostProcessor;
import org.springframework.amqp.support.postprocessor.UnzipPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ErrorHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
class RabbitConfiguringPostProcessor implements BeanPostProcessor {

  private static final DelegatingDecompressingPostProcessor DECOMPRESSING_POST_PROCESSOR;

  static {
    DelegatingDecompressingPostProcessor postProcessor =
        new DelegatingDecompressingPostProcessor();

    postProcessor.addDecompressor("lz4", new Lz4DecompressMessagePostProcessor());
    postProcessor.addDecompressor("zip", new UnzipPostProcessor());
    postProcessor.addDecompressor("gzip", new GUnzipPostProcessor());

    DECOMPRESSING_POST_PROCESSOR = postProcessor;
  }

  private final MessagingProperties properties;
  private final Collection<ReceiveMessageListener> listeners;
  @Setter
  private AmqpMessageEncypter messageEncrypter;
  @Setter
  private AmqpMessageDecrypter messageDecrypter;
  @Setter
  private ErrorHandler errorHandler;

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

    if (messageEncrypter != null)
      template.addBeforePublishPostProcessors(new EncryptMessagePostProcessor(messageEncrypter));

    template.addAfterReceivePostProcessors(DECOMPRESSING_POST_PROCESSOR);

    if (messageDecrypter != null)
      template.addAfterReceivePostProcessors(new DecryptMessagePostProcessor(messageDecrypter));
  }

  private void configureContainerFactory(AbstractRabbitListenerContainerFactory<?> factory) {
    if (errorHandler != null)
      factory.setErrorHandler(errorHandler);

    List<MessagePostProcessor> postProcessors = new ArrayList<>();

    postProcessors.add(DECOMPRESSING_POST_PROCESSOR);

    if (messageDecrypter != null)
      postProcessors.add(new DecryptMessagePostProcessor(messageDecrypter));

    if (!isEmpty(listeners))
      postProcessors.add(new ListeningMessagePostProcessor(listeners));

    factory.setAfterReceivePostProcessors(postProcessors.toArray(MessagePostProcessor[]::new));
  }
}
