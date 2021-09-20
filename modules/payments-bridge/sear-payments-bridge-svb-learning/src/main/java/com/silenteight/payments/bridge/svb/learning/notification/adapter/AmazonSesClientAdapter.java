package com.silenteight.payments.bridge.svb.learning.notification.adapter;

import com.silenteight.payments.bridge.svb.learning.notification.port.outgoing.AmazonSesClient;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!mockaws")
class AmazonSesClientAdapter implements AmazonSesClient {

  @Override
  public void sendEmail(SendRawEmailRequest request) {
    var client =
        AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_WEST_1).build();
    client.sendRawEmail(request);
  }
}
