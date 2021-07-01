package com.silenteight.serp.governance.model.accept;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.model.accept.amqp.ModelPromotedMessageGateway;

@RequiredArgsConstructor
public class SendPromoteMessageUseCase {

  @NonNull
  private final ModelPromotedMessageGateway messageGateway;

  public void activate(SendPromoteMessageCommand command) {
    messageGateway.send(command.toMessage());
  }
}
