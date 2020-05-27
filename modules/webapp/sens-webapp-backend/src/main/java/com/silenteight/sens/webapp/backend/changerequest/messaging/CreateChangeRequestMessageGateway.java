package com.silenteight.sens.webapp.backend.changerequest.messaging;

import com.silenteight.proto.serp.v1.changerequest.CreateChangeRequestCommand;

public interface CreateChangeRequestMessageGateway {

  void send(CreateChangeRequestCommand command);
}
