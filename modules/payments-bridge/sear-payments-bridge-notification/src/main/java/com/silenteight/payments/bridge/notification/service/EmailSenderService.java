package com.silenteight.payments.bridge.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.environment.EnvironmentProperties;
import com.silenteight.payments.bridge.notification.model.NonRecoverableEmailSendingException;
import com.silenteight.payments.bridge.notification.model.SendEmailRequest;
import com.silenteight.payments.bridge.notification.port.EmailSenderUseCase;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties({ EmailNotificationProperties.class, EnvironmentProperties.class })
class EmailSenderService implements EmailSenderUseCase {

  private static final String ATTACHMENT_TYPE_ZIP = "application/zip";

  private final EmailNotificationProperties emailNotificationProperties;
  private final JavaMailSender emailSender;

  public void sendEmail(SendEmailRequest sendEmailRequest) {
    MimeMessagePreparator messagePreparator = mimeMessage ->
        prepareMessageHelper(sendEmailRequest, mimeMessage);

    try {
      log.debug("Starting an attempt to send an email, notification ids={}",
          sendEmailRequest.getIds());
      emailSender.send(messagePreparator);
    } catch (MailAuthenticationException | MailParseException |
        MailPreparationException exception) {
      log.error(
          "Mail cannot be sent due to authentication, parse or "
              + "preparation exception. Message= {}, reason= {}. Notifications ids={}",
          exception.getMessage(), exception.getCause(), sendEmailRequest.getIds());
      throw new NonRecoverableEmailSendingException("Mail could not be sent.");
    }
    log.debug("Successful attempt to send an email, notification ids={}",
        sendEmailRequest.getIds());
  }

  private void prepareMessageHelper(
      SendEmailRequest sendEmailRequest, MimeMessage mimeMessage) throws MessagingException {

    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
    messageHelper.setFrom(emailNotificationProperties.getFrom());
    messageHelper.setTo(emailNotificationProperties.getTo());
    setCc(messageHelper);
    messageHelper.setSubject(sendEmailRequest.getSubject());
    messageHelper.setText(sendEmailRequest.getHtmlText(), true);
    setAttachment(sendEmailRequest, messageHelper);

  }

  private static void setAttachment(
      SendEmailRequest sendEmailRequest, MimeMessageHelper messageHelper) throws
      MessagingException {
    if (sendEmailRequest.getAttachmentName() != null && sendEmailRequest.getAttachment() != null) {
      messageHelper.addAttachment(
          sendEmailRequest.getAttachmentName(),
          new ByteArrayDataSource(sendEmailRequest.getAttachment(), ATTACHMENT_TYPE_ZIP));
    }
  }

  private void setCc(MimeMessageHelper messageHelper) throws MessagingException {
    String cc = emailNotificationProperties.getCc();
    if (isNotBlank(cc)) {
      messageHelper.setCc(cc);
    }
  }
}
