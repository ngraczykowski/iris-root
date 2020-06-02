package com.silenteight.sens.webapp.backend.changerequest.reject;

import com.silenteight.proto.serp.v1.changerequest.RejectChangeRequestCommand;

interface RejectChangeRequestMessageGateway {

  void send(RejectChangeRequestCommand message);
}
