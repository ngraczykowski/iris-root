package com.silenteight.payments.bridge.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.notification.model.NonRecoverableEmailSendingException;
import com.silenteight.payments.bridge.notification.port.EmailSenderUseCase;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(EmailNotificationProperties.class)
class EmailSenderService implements EmailSenderUseCase {

  private static final String ATTACHMENT_TYPE_ZIP = "application/zip";

  private final EmailNotificationProperties emailNotificationProperties;
  private final JavaMailSender emailSender;

  @Async
  public void sendEmail(
      Long id, String subject, String htmlText, String attachmentName, byte[] attachment) {
    MimeMessagePreparator messagePreparator = mimeMessage ->
        prepareMessageHelper(subject, htmlText, attachmentName, attachment, mimeMessage);

    try {
      emailSender.send(messagePreparator);
    } catch (MailAuthenticationException | MailParseException |
        MailPreparationException exceptions) {
      log.error("Mail cannot be sent due to authentication, parse or "
          + "preparation exception. Notification id={}", id);
      throw new NonRecoverableEmailSendingException("Mail could not be sent");
    }
  }

  private void prepareMessageHelper(
      String subject, String htmlText, String attachmentName, byte[] attachment,
      MimeMessage mimeMessage) throws MessagingException {
    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
    messageHelper.setFrom(emailNotificationProperties.getFrom());
    messageHelper.setTo(emailNotificationProperties.getTo());
    messageHelper.setSubject(subject);
    messageHelper.setText(htmlText, true);

    messageHelper.addAttachment(
        attachmentName, new ByteArrayDataSource(attachment, ATTACHMENT_TYPE_ZIP));
  }
}
