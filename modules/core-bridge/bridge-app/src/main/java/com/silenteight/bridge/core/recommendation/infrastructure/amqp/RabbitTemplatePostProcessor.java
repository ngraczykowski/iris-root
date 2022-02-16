package com.silenteight.bridge.core.recommendation.infrastructure.amqp;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class RabbitTemplatePostProcessor implements BeanPostProcessor {

  private static final Lz4DecompressingPostProcessor LZ4_DECOMPRESSING_POST_PROCESSOR =
      new Lz4DecompressingPostProcessor();

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws
      BeansException {
    if (bean instanceof RabbitTemplate template) {
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
    template.addAfterReceivePostProcessors(LZ4_DECOMPRESSING_POST_PROCESSOR);
  }
}
