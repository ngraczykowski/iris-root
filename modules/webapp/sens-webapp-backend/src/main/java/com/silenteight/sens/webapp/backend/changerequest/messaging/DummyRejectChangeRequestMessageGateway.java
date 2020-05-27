package com.silenteight.sens.webapp.backend.changerequest.messaging;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.changerequest.RejectChangeRequestCommand;
import com.silenteight.sens.webapp.backend.changerequest.reject.RejectChangeRequestMessageHandler;

@RequiredArgsConstructor
class DummyRejectChangeRequestMessageGateway implements RejectChangeRequestMessageGateway {

  @NonNull
  private final RejectChangeRequestMessageHandler handler;

  @Override
  public void send(RejectChangeRequestCommand message) {
    handler.handle(message);
  }
}
