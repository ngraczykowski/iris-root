package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnClass(AmqpMessagingConfiguration.class)
class RabbitTemplatePostProcessor implements BeanPostProcessor {

  public static final int FASTEST_COMPRESSION = 1;
  private static final Lz4DecompressingPostProcessor
      LZ4_DECOMPRESSING_POST_PROCESSOR = new Lz4DecompressingPostProcessor();

  private final AmqpProtoMessageConverter amqpProtoMessageConverter;

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws
      BeansException {
    if (bean instanceof RabbitTemplate) {
      var template = (RabbitTemplate) bean;
      configureRabbitTemplate(template);
    }
    if (AbstractRabbitListenerContainerFactory.class.isAssignableFrom(bean.getClass())) {
      configureContainerFactory((AbstractRabbitListenerContainerFactory<?>) bean);
    }

    return bean;
  }

  private static void configureContainerFactory(AbstractRabbitListenerContainerFactory<?> factory) {
    factory.setAfterReceivePostProcessors(LZ4_DECOMPRESSING_POST_PROCESSOR);
  }

  private void configureRabbitTemplate(RabbitTemplate template) {
    template.setMessageConverter(amqpProtoMessageConverter);

    template.addAfterReceivePostProcessors(LZ4_DECOMPRESSING_POST_PROCESSOR);
    template.addBeforePublishPostProcessors(new Lz4CompressingPostProcessor(FASTEST_COMPRESSION));
  }
}
