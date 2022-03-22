package com.silenteight.agent.common.messaging;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ErrorHandler;

@RequiredArgsConstructor
class RabbitConfiguringPostProcessor implements BeanPostProcessor {

  private final AgentMessagePostProcessors messagePostProcessors;
  private final ErrorHandler errorHandler;

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

    template.addAfterReceivePostProcessors(getReceivePostProcessor());
    template.addBeforePublishPostProcessors(getPublishPostProcessor());
  }

  private void configureContainerFactory(AbstractRabbitListenerContainerFactory<?> factory) {
    if (errorHandler != null) {
      factory.setErrorHandler(errorHandler);
    }

    factory.setAfterReceivePostProcessors(getReceivePostProcessor());
  }

  private MessagePostProcessor getReceivePostProcessor() {
    return messagePostProcessors.getReceivePostProcessor();
  }

  private MessagePostProcessor getPublishPostProcessor() {
    return messagePostProcessors.getPublishPostProcessor();
  }
}
