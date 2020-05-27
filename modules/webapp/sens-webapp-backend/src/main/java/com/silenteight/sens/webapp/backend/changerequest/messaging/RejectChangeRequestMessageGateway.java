package com.silenteight.sens.webapp.backend.changerequest.messaging;

import com.silenteight.proto.serp.v1.changerequest.RejectChangeRequestCommand;

public interface RejectChangeRequestMessageGateway {

  void send(RejectChangeRequestCommand message);
}
