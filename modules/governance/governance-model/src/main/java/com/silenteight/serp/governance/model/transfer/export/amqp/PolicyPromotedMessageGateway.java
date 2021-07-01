package com.silenteight.serp.governance.model.transfer.export.amqp;

import com.silenteight.model.api.v1.ModelPromotedForProduction;

public interface PolicyPromotedMessageGateway {

  void send(ModelPromotedForProduction message);
}
