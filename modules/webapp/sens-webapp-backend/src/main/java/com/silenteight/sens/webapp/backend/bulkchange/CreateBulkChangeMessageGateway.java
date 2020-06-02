package com.silenteight.sens.webapp.backend.bulkchange;

import com.silenteight.proto.serp.v1.governance.CreateBulkBranchChangeCommand;

public interface CreateBulkChangeMessageGateway {

  void send(CreateBulkBranchChangeCommand command);
}
