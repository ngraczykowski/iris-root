package com.silenteight.serp.governance.model.accept.amqp;

import com.silenteight.model.api.v1.ModelPromotedForProduction;

public interface ModelPromotedMessageGateway {

  void send(ModelPromotedForProduction message);
}
