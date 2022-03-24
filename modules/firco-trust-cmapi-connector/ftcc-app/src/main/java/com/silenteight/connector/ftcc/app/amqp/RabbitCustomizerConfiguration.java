package com.silenteight.connector.ftcc.app.amqp;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.callback.exception.CallbackException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@Slf4j
class RabbitCustomizerConfiguration {

  @Bean
  RabbitRetryTemplateCustomizer rabbitRetryTemplateCustomizer(RabbitProperties rabbitProperties) {
    return new RetryTemplateCustomizer(rabbitProperties);
  }

  @Bean
  MessageRecoverer messageRecoverer() {
    return new RejectAndDontRequeueRecoverer() {
      @Override
      public void recover(Message message, Throwable cause) {
        findCallbackException(cause)
            .ifPresent(exception ->
                // TODO: implement send Undelivered message;
                log.warn("Send undelivered messageId={TODO}")
            );
        super.recover(message, cause);
      }
    };
  }

  private static Optional<CallbackException> findCallbackException(Throwable cause) {
    Throwable exception = cause;
    while (exception != null &&
        !CallbackException.class.isAssignableFrom(exception.getClass())) {
      exception = exception.getCause();
    }
    return Optional.ofNullable((CallbackException) exception);
  }
}
