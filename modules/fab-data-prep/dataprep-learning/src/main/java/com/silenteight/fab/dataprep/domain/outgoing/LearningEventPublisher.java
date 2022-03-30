package com.silenteight.fab.dataprep.domain.outgoing;

import com.silenteight.fab.dataprep.domain.model.WarehouseEvent;

public interface LearningEventPublisher {

  void publish(WarehouseEvent event);
}
