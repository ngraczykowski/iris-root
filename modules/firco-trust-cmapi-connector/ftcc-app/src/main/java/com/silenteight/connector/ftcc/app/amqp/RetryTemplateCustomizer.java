package com.silenteight.connector.ftcc.app.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.connector.ftcc.callback.exception.NonRecoverableCallbackException;
import com.silenteight.connector.ftcc.callback.exception.RecoverableCallbackException;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties.ContainerType;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@EnableConfigurationProperties(RabbitProperties.class)
@RequiredArgsConstructor
class RetryTemplateCustomizer implements RabbitRetryTemplateCustomizer {

  private static final Map<Class<? extends Throwable>, Boolean> RETRYABLE_CLASSIFIER = Map.of(
      NonRecoverableCallbackException.class, FALSE,
      RecoverableCallbackException.class, TRUE);

  private final RabbitProperties properties;

  @Override
  public void customize(Target target, RetryTemplate retryTemplate) {
    if (Target.LISTENER == target) {
      var containerType = properties.getListener().getType();
      if (containerType == ContainerType.SIMPLE) {
        var maxAttempts = properties.getListener().getSimple().getRetry().getMaxAttempts();
        retryTemplate.setRetryPolicy(createPolicy(maxAttempts));
      } else if (containerType == ContainerType.DIRECT) {
        var maxAttempts = properties.getListener().getDirect().getRetry().getMaxAttempts();
        retryTemplate.setRetryPolicy(createPolicy(maxAttempts));
      }
    }
  }

  @NotNull
  private static RetryPolicy createPolicy(int maxAttempts) {
    return maxAttempts > 0 ? new SimpleRetryPolicy(maxAttempts, RETRYABLE_CLASSIFIER, true)
                           : new AlwaysRetryPolicy();
  }
}
