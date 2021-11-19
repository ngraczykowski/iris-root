package com.silenteight.serp.governance.model.archive.amqp;

import com.silenteight.model.api.v1.ModelsArchived;

public interface ModelsArchivedMessageGateway {

  void send(ModelsArchived message);
}
