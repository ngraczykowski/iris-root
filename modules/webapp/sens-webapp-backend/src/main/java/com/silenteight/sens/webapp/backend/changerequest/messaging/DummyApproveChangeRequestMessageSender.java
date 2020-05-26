package com.silenteight.sens.webapp.backend.changerequest.messaging;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.changerequest.ApproveChangeRequestCommand;
import com.silenteight.sens.webapp.backend.changerequest.approve.ApproveChangeRequestMessageHandler;

@RequiredArgsConstructor
class DummyApproveChangeRequestMessageSender implements ApproveChangeRequestMessageSender {

  @NonNull
  private final ApproveChangeRequestMessageHandler handler;

  @Override
  public void send(ApproveChangeRequestCommand message) {
    handler.handle(message);
  }
}
