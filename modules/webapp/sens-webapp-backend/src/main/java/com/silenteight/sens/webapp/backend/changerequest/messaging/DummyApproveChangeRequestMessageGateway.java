package com.silenteight.sens.webapp.backend.changerequest.messaging;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.changerequest.ApproveChangeRequestCommand;
import com.silenteight.sens.webapp.backend.changerequest.approve.ApproveChangeRequestMessageHandler;

@RequiredArgsConstructor
class DummyApproveChangeRequestMessageGateway implements ApproveChangeRequestMessageGateway {

  @NonNull
  private final ApproveChangeRequestMessageHandler handler;

  @Override
  public void send(ApproveChangeRequestCommand command) {
    handler.handle(command);
  }
}
