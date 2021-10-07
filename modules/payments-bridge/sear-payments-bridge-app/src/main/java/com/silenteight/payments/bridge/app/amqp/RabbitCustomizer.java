package com.silenteight.payments.bridge.app.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.event.AlertUndeliveredEvent;
import com.silenteight.payments.bridge.firco.callback.model.CallbackException;
import com.silenteight.payments.bridge.firco.callback.model.NonRecoverableCallbackException;
import com.silenteight.payments.bridge.firco.callback.model.RecoverableCallbackException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
class RabbitCustomizer {

  private final CommonChannels commonChannels;

  @Bean
  RabbitRetryTemplateCustomizer rabbitRetryTemplateCustomizer() {
    return new RetryTemplateCustomizer();
  }

  private static class RetryTemplateCustomizer
      implements RabbitRetryTemplateCustomizer {

    @Override
    public void customize(Target target, RetryTemplate retryTemplate) {
      Map<Class<? extends Throwable>, Boolean> retryableClassifier = new HashMap<>();
      retryableClassifier.put(NonRecoverableCallbackException.class, false);
      retryableClassifier.put(RecoverableCallbackException.class, true);

      var backOffPolicy = new ExponentialBackOffPolicy();
      backOffPolicy.setInitialInterval(200);
      backOffPolicy.setMultiplier(2);

      if (Target.LISTENER.equals(target)) {
        retryTemplate.setRetryPolicy(
            new SimpleRetryPolicy(8, retryableClassifier, true));
        retryTemplate.setBackOffPolicy(backOffPolicy);
      }
    }
  }

  @Bean
  MessageRecoverer messageRecoverer() {
    return new RejectAndDontRequeueRecoverer() {
      @Override
      public void recover(Message message, Throwable cause) {
        findCallbackException(cause)
            .ifPresent(exception ->
              commonChannels.undeliveredAlertChannel().send(
                  MessageBuilder.withPayload(
                      new AlertUndeliveredEvent(exception.getAlertId(),
                          exception.getStatus().name())).build())
            );
        super.recover(message, cause);
      }
    };
  }

  private Optional<CallbackException> findCallbackException(Throwable cause) {
    Throwable exception = cause;
    while (exception != null &&
        !CallbackException.class.isAssignableFrom(exception.getClass())) {
      exception = exception.getCause();
    }
    return Optional.ofNullable((CallbackException)exception);
  }

}
