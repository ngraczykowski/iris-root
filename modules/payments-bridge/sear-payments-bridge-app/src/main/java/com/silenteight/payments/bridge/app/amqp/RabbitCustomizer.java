package com.silenteight.payments.bridge.app.amqp;

import com.silenteight.payments.bridge.common.exception.NonRecoverableOperationException;
import com.silenteight.payments.bridge.common.exception.RecoverableOperationException;

import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
class RabbitCustomizer {

  @Bean
  RabbitRetryTemplateCustomizer rabbitRetryTemplateCustomizer() {
    return new RetryTemplateCustomizer();
  }

  private static class RetryTemplateCustomizer
      implements RabbitRetryTemplateCustomizer {

    @Override
    public void customize(Target target, RetryTemplate retryTemplate) {
      Map<Class<? extends Throwable>, Boolean> retryableClassifier = new HashMap<>();
      retryableClassifier.put(NonRecoverableOperationException.class, false);
      retryableClassifier.put(RecoverableOperationException.class, true);

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

}
