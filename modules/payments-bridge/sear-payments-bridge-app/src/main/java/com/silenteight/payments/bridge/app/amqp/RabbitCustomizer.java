package com.silenteight.payments.bridge.app.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.callback.model.CallbackException;
import com.silenteight.payments.bridge.firco.callback.model.NonRecoverableCallbackException;
import com.silenteight.payments.bridge.firco.callback.model.RecoverableCallbackException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@EnableConfigurationProperties(RabbitProperties.class)
@Configuration
@RequiredArgsConstructor
class RabbitCustomizer {

  private final RabbitProperties rabbitProperties;
  private final AlertUndeliveredPort alertUndeliveredPort;

  @Bean
  RabbitRetryTemplateCustomizer rabbitRetryTemplateCustomizer() {
    return new RetryTemplateCustomizer();
  }

  private class RetryTemplateCustomizer
      implements RabbitRetryTemplateCustomizer {

    @Override
    public void customize(Target target, RetryTemplate retryTemplate) {
      Map<Class<? extends Throwable>, Boolean> retryableClassifier = new HashMap<>();
      retryableClassifier.put(NonRecoverableCallbackException.class, false);
      retryableClassifier.put(RecoverableCallbackException.class, true);

      var maxAttempts = rabbitProperties.getListener().getSimple().getRetry().getMaxAttempts();
      if (Target.LISTENER.equals(target)) {
        retryTemplate.setRetryPolicy(
            new SimpleRetryPolicy(maxAttempts, retryableClassifier, true));
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
                alertUndeliveredPort.sendUndelivered(
                    exception.getAlertId(), exception.getStatus()));
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
