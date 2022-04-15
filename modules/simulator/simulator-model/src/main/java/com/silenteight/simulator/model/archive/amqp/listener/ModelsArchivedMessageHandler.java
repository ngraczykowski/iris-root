package com.silenteight.simulator.model.archive.amqp.listener;

import lombok.NonNull;

import com.silenteight.model.api.v1.ModelsArchived;

public interface ModelsArchivedMessageHandler {

  void handle(@NonNull ModelsArchived message);
}
