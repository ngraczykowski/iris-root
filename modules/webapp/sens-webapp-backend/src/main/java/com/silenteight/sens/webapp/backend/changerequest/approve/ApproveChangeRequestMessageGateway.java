package com.silenteight.sens.webapp.backend.changerequest.approve;

import com.silenteight.proto.serp.v1.changerequest.ApproveChangeRequestCommand;

interface ApproveChangeRequestMessageGateway {

  void send(ApproveChangeRequestCommand message);
}
