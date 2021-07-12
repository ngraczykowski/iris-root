package com.silenteight.serp.governance.qa.send;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.send.amqp.AlertMessageGateway;

@RequiredArgsConstructor
public class SendAlertMessageUseCase {

  @NonNull
  private final AlertMessageGateway messageGateway;

  public void activate(SendAlertMessageCommand command) {
    messageGateway.send(command.toMessage());
  }
}
