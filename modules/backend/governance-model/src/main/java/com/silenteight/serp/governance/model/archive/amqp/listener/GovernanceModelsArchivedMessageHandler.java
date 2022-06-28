package com.silenteight.serp.governance.model.archive.amqp.listener;

import lombok.NonNull;

import com.silenteight.model.api.v1.ModelsArchived;

public interface GovernanceModelsArchivedMessageHandler {

  void handle(@NonNull ModelsArchived message);
}
