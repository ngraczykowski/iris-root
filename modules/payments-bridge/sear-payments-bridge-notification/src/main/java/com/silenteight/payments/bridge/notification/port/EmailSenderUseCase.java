package com.silenteight.payments.bridge.notification.port;

import org.springframework.mail.MailSendException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

public interface EmailSenderUseCase {

  @Retryable(value = MailSendException.class,
      maxAttempts = 5, backoff = @Backoff(delay = 5000))
  void sendEmail(
      Long id, String subject, String htmlText, String attachmentName, byte[] attachment);
}
