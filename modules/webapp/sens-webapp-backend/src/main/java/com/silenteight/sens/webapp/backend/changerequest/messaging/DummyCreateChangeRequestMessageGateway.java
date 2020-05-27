package com.silenteight.sens.webapp.backend.changerequest.messaging;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.changerequest.CreateChangeRequestCommand;
import com.silenteight.sens.webapp.backend.changerequest.create.CreateChangeRequestMessageHandler;

@RequiredArgsConstructor
class DummyCreateChangeRequestMessageGateway implements CreateChangeRequestMessageGateway {

  @NonNull
  private final CreateChangeRequestMessageHandler createChangeRequestMessageHandler;

  @Override
  public void send(CreateChangeRequestCommand command) {
    createChangeRequestMessageHandler.handle(command);
  }
}
