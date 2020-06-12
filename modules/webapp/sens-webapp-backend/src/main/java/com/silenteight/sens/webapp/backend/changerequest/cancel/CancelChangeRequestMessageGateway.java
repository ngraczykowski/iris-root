package com.silenteight.sens.webapp.backend.changerequest.cancel;

import com.silenteight.proto.serp.v1.changerequest.CancelChangeRequestCommand;

interface CancelChangeRequestMessageGateway {

  void send(CancelChangeRequestCommand message);
}
