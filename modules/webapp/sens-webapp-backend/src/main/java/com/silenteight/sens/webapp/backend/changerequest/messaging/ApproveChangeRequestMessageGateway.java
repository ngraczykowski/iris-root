package com.silenteight.sens.webapp.backend.changerequest.messaging;

import com.silenteight.proto.serp.v1.changerequest.ApproveChangeRequestCommand;

public interface ApproveChangeRequestMessageGateway {

  void send(ApproveChangeRequestCommand message);
}
