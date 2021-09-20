package com.silenteight.payments.bridge.svb.learning.notification.port.outgoing;

import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;

public interface AmazonSesClient {

  void sendEmail(SendRawEmailRequest request);
}
