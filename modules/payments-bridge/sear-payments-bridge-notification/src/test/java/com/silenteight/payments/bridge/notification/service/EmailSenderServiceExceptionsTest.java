package com.silenteight.payments.bridge.notification.service;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.notification.NotificationModule;
import com.silenteight.payments.bridge.notification.model.NonRecoverableEmailSendingException;
import com.silenteight.payments.bridge.notification.model.SendEmailRequest;
import com.silenteight.payments.bridge.notification.port.EmailSenderUseCase;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@JdbcTest
@PropertySource("classpath:config/application-test.yml")
@ComponentScan(basePackageClasses = NotificationModule.class)
@EnableConfigurationProperties(EmailNotificationProperties.class)
@SpringIntegrationTest
class EmailSenderServiceExceptionsTest extends BaseJdbcTest {

  @MockBean
  private JavaMailSender emailSender;

  @Autowired
  private EmailSenderUseCase emailSenderUseCase;

  @Test
  void sendEmail_providedEmailSenderThrowingMailSendException_numberOfAttemptsIs5() {

    doThrow(MailSendException.class).when(emailSender).send(any(MimeMessagePreparator.class));

    Long id = 0L;
    String subject = "Test subject";
    String htmlText = "<html>\n";
    String attachmentName = "fil3e.zip";
    byte[] attachment = new byte[0];

    var sendEmailRequest = SendEmailRequest.builder()
        .ids(List.of(id))
        .subject(subject)
        .htmlText(htmlText)
        .attachmentName(attachmentName)
        .attachment(attachment)
        .build();

    assertThatExceptionOfType(MailSendException.class).isThrownBy(
        () -> emailSenderUseCase.sendEmail(sendEmailRequest));
    verify(emailSender, times(5)).send(any(MimeMessagePreparator.class));
  }

  @Test
  void sendEmail_providedEmailSenderThrowingOtherThanMailSendException_RuntimeIsThrown() {

    Long id = 0L;
    String subject = "Test subject";
    String htmlText = "<html>\n";
    String attachmentName = "fil3e.zip";
    byte[] attachment = new byte[0];

    var sendEmailRequest = SendEmailRequest.builder()
        .ids(List.of(id))
        .subject(subject)
        .htmlText(htmlText)
        .attachmentName(attachmentName)
        .attachment(attachment)
        .build();

    doThrow(MailAuthenticationException.class)
        .when(emailSender)
        .send(any(MimeMessagePreparator.class));
    assertThrows(
        NonRecoverableEmailSendingException.class,
        () -> emailSenderUseCase.sendEmail(sendEmailRequest));

    doThrow(MailParseException.class).when(emailSender).send(any(MimeMessagePreparator.class));
    assertThrows(
        NonRecoverableEmailSendingException.class,
        () -> emailSenderUseCase.sendEmail(sendEmailRequest));

    doThrow(MailPreparationException.class)
        .when(emailSender)
        .send(any(MimeMessagePreparator.class));
    assertThrows(
        NonRecoverableEmailSendingException.class,
        () -> emailSenderUseCase.sendEmail(sendEmailRequest));
  }
}
