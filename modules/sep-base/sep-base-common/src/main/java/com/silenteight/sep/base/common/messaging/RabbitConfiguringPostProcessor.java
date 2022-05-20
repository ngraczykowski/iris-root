package com.silenteight.sep.base.common.messaging;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.sep.base.common.messaging.postprocessing.SepMessagePostProcessors;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ErrorHandler;

import java.util.ArrayList;
import java.util.Collection;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
class RabbitConfiguringPostProcessor implements BeanPostProcessor {
  private final SepMessagePostProcessors sepMessagePostProcessors;
  private final Collection<ReceiveMessageListener> listeners;
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

    template.addBeforePublishPostProcessors(sepMessagePostProcessors.getSendPostProcessor());
    template.addAfterReceivePostProcessors(sepMessagePostProcessors.getReceivePostProcessor());
  }

  private void configureContainerFactory(AbstractRabbitListenerContainerFactory<?> factory) {
    if (errorHandler != null)
      factory.setErrorHandler(errorHandler);

    var postProcessors = new ArrayList<MessagePostProcessor>();
    postProcessors.add(sepMessagePostProcessors.getReceivePostProcessor());

    if (!isEmpty(listeners))
      postProcessors.add(new ListeningMessagePostProcessor(listeners));

    factory.setAfterReceivePostProcessors(postProcessors.toArray(MessagePostProcessor[]::new));
  }
}
