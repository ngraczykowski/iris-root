package com.silenteight.payments.bridge.svb.learning.notification.port.outgoing;

import software.amazon.awssdk.services.ses.model.SendRawEmailRequest;

public interface AmazonSesClient {

  void sendEmail(SendRawEmailRequest request);
}
