package com.silenteight.sens.webapp.backend.changerequest.create;

import com.silenteight.proto.serp.v1.changerequest.CreateChangeRequestCommand;

public interface CreateChangeRequestMessageGateway {

  void send(CreateChangeRequestCommand command);
}
