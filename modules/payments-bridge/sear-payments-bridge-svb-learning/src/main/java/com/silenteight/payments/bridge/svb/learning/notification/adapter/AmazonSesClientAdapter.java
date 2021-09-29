package com.silenteight.payments.bridge.svb.learning.notification.adapter;

import com.silenteight.payments.bridge.svb.learning.notification.port.outgoing.AmazonSesClient;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest;

@Service
@Profile("!mockaws")
class AmazonSesClientAdapter implements AmazonSesClient {

  @Override
  public void sendEmail(SendRawEmailRequest request) {
    try (var ses = SesClient.builder().build()) {
      ses.sendRawEmail(request);
    } catch (Exception e) {
      throw new RuntimeException("Couldn't send notification email message = " + e.getMessage());
    }
  }
}
