package com.silenteight.serp.governance.model.transfer.export;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.model.transfer.export.amqp.PolicyPromotedMessageGateway;

@RequiredArgsConstructor
public class SendPromoteMessageUseCase {

  @NonNull
  private final PolicyPromotedMessageGateway messageGateway;

  public void activate(SendPromoteMessageCommand command) {
    messageGateway.send(command.toMessage());
  }
}
