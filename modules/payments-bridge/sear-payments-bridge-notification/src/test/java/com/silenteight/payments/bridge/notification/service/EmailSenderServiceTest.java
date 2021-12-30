package com.silenteight.payments.bridge.notification.service;

import com.silenteight.payments.bridge.notification.NotificationModule;
import com.silenteight.payments.bridge.notification.model.SendEmailRequest;
import com.silenteight.payments.bridge.notification.port.EmailSenderUseCase;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;
import javax.mail.internet.MimeMessage;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@PropertySource("classpath:config/application-test.yml")
@ComponentScan(basePackageClasses = NotificationModule.class)
@EnableConfigurationProperties(EmailNotificationProperties.class)
@Import(EmailSenderConfiguration.class)
class EmailSenderServiceTest extends BaseJdbcTest {

  @RegisterExtension
  static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
      .withConfiguration(GreenMailConfiguration
          .aConfig()
          .withUser("user", "password"))
      .withPerMethodLifecycle(false);

  @Autowired
  private EmailSenderUseCase emailSenderUseCase;

  @Autowired
  private EmailNotificationProperties emailNotificationProperties;

  @Test
  void sendEmail_providedDataAndEnvIsValid() throws Exception {

    Long id = 0L;
    String subject = "Silent Eight - Learning data";
    String htmlText = "<html>\n"
        + "<body>\n"
        + "\n"
        + "<p>This is to confirm Silent Eight has received a CSV file which "
        + "has been processed by SEAR.</p>\n"
        + "<p>File name: 06-06-2021.csv</p>\n"
        + "<p>Number of records: 29</p>\n"
        + "<p>File size: 62,0 kB</p>\n"
        + "<p></p>\n"
        + "<p>Thank you.</p>\n"
        + "<p>Silent Eight</p>\n"
        + "\n"
        + "</body>\n"
        + "</html>";
    String attachmentName = "file.zip";
    byte[] attachment = createByteArrayFromFile();

    var sendEmailRequest = SendEmailRequest.builder()
        .ids(List.of(id))
        .subject(subject)
        .htmlText(htmlText)
        .attachmentName(attachmentName)
        .attachment(attachment)
        .build();

    emailSenderUseCase.sendEmail(sendEmailRequest);

    await().atMost(2, SECONDS).untilAsserted(() -> {
      MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
      MimeMessage receivedMessage = receivedMessages[0];
      assertEquals(1, receivedMessages.length);
      assertEquals(1, receivedMessage.getAllRecipients().length);
      assertEquals(
          emailNotificationProperties.getTo(),
          receivedMessage.getAllRecipients()[0].toString());
    });
  }

  private byte[] createByteArrayFromFile() throws Exception {
    URL url = getClass().getResource("/file.zip");
    File file = new File(url.getPath());

    FileInputStream fileInputStream = new FileInputStream(file);
    byte[] data = new byte[(int) file.length()];
    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
    bufferedInputStream.read(data, 0, data.length);

    return data;
  }
}


