package com.silenteight.payments.bridge.svb.learning.notification.service.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.notification.model.NotificationRequest;
import com.silenteight.payments.bridge.svb.learning.notification.port.outgoing.AmazonSesClient;
import com.silenteight.payments.bridge.svb.learning.notification.port.outgoing.SendNotificationUseCase;

import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.validation.Valid;

import static com.silenteight.payments.bridge.svb.learning.notification.service.outgoing.GenerateLogFileUseCase.generateLogFile;

@Slf4j
@RequiredArgsConstructor
class SendNotificationService implements SendNotificationUseCase {

  private final AmazonSesClient amazonSesClient;
  @Valid
  private final SendNotificationProperties properties;

  public void sendNotification(NotificationRequest request) {
    try {

      var outputStream = new ByteArrayOutputStream();

      var message = createMessage(request);
      message.writeTo(outputStream);

      var rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

      var rawEmailRequest = new SendRawEmailRequest(rawMessage);

      amazonSesClient.sendEmail(rawEmailRequest);

    } catch (Exception ex) {
      log.error("There was a problem when sending email = {}", ex.getMessage());
    }
  }

  private MimeMessage createMessage(NotificationRequest request) throws MessagingException {
    var session = Session.getDefaultInstance(new Properties());
    var message = new MimeMessage(session);
    var msg = new MimeMultipart("mixed");

    message.setSubject(properties.getSubject(), "UTF-8");
    message.setFrom(new InternetAddress(properties.getSender()));
    message.setRecipients(
        Message.RecipientType.TO, InternetAddress.parse(properties.getRecipient()));

    msg.addBodyPart(createBodyPart(request));

    message.setContent(msg);

    return message;
  }

  private static MimeBodyPart createBodyPart(NotificationRequest request) throws
      MessagingException {
    var wrap = new MimeBodyPart();
    var textPart = new MimeBodyPart();
    var htmlPart = new MimeBodyPart();
    var msgBody = new MimeMultipart("alternative");

    textPart.setContent(request.toBodyText(), "text/plain; charset=UTF-8");
    htmlPart.setContent(request.toBodyHtml(), "text/html; charset=UTF-8");

    msgBody.addBodyPart(textPart);
    msgBody.addBodyPart(htmlPart);

    if (request.getFailedRecord() > 0) {
      msgBody.addBodyPart(createFilePart(request));
    }

    wrap.setContent(msgBody);

    return wrap;
  }

  private static MimeBodyPart createFilePart(NotificationRequest request) throws
      MessagingException {
    MimeBodyPart att = new MimeBodyPart();
    DataSource fds =
        new FileDataSource(generateLogFile(request.getErrorLogs()));
    att.setDataHandler(new DataHandler(fds));
    att.setFileName(fds.getName());
    return att;
  }
}
