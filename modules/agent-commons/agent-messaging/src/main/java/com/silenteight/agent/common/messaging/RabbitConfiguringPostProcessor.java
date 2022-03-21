package com.silenteight.agent.common.messaging;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.agent.common.messaging.compression.Lz4CompressingPostProcessor;
import com.silenteight.agent.common.messaging.compression.Lz4DecompressingPostProcessor;

import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ErrorHandler;

@RequiredArgsConstructor
class RabbitConfiguringPostProcessor implements BeanPostProcessor {

  private static final int FASTEST_COMPRESSION = 1;
  private static final Lz4DecompressingPostProcessor
      LZ4_DECOMPRESSING_POST_PROCESSOR = new Lz4DecompressingPostProcessor();

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
    if (errorHandler != null) {
      template.setReplyErrorHandler(errorHandler);
    }

    template.addAfterReceivePostProcessors(LZ4_DECOMPRESSING_POST_PROCESSOR);
    template.addBeforePublishPostProcessors(new Lz4CompressingPostProcessor(FASTEST_COMPRESSION));
  }

  private void configureContainerFactory(AbstractRabbitListenerContainerFactory<?> factory) {
    if (errorHandler != null) {
      factory.setErrorHandler(errorHandler);
    }
  }
}
