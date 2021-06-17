package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnClass(AmqpMessagingConfiguration.class)
class RabbitTemplatePostProcessor implements BeanPostProcessor {

  private final AmqpProtoMessageConverter amqpProtoMessageConverter;

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws
      BeansException {
    if (bean instanceof RabbitTemplate) {
      var template = (RabbitTemplate) bean;
      template.setMessageConverter(amqpProtoMessageConverter);
    }

    return bean;
  }
}
