package com.silenteight.payments.bridge.svb.learning.notification.adapter;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.notification.port.outgoing.AmazonSesClient;

import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Profile("mockaws")
class AmazonSesClientMockAdapter implements AmazonSesClient {

  @Override
  public void sendEmail(SendRawEmailRequest request) {
    log.info("Mocking send email request");
  }
}
