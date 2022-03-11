package com.silenteight.agent.common.messaging;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ErrorHandler;

@RequiredArgsConstructor
class RabbitConfiguringPostProcessor implements BeanPostProcessor {

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
  }

  private void configureContainerFactory(AbstractRabbitListenerContainerFactory<?> factory) {
    if (errorHandler != null) {
      factory.setErrorHandler(errorHandler);
    }
  }
}
