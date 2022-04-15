package com.silenteight.serp.governance.model.accept;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.model.accept.amqp.ModelPromotedMessageGateway;

@Slf4j
@RequiredArgsConstructor
public class SendPromoteMessageUseCase {

  @NonNull
  private final ModelPromotedMessageGateway messageGateway;

  public void activate(SendPromoteMessageCommand command) {
    log.info("Sending ModelPromotedForProduction for modelName={}, modelVersion={}",
        command.getModelName(), command.getModelVersion());

    messageGateway.send(command.toMessage());
  }
}
