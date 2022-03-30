package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.LearningData;
import com.silenteight.fab.dataprep.domain.model.WarehouseEvent;
import com.silenteight.fab.dataprep.domain.model.WarehouseEvent.Alert;
import com.silenteight.fab.dataprep.domain.outgoing.LearningEventPublisher;

import org.springframework.stereotype.Service;

import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
class LearningService {

  private final LearningEventPublisher learningEventPublisher;

  public void feedWarehouse(LearningData learningData) {
    WarehouseEvent warehouseEvent = createEvent(learningData);

    learningEventPublisher.publish(warehouseEvent);
  }

  private static WarehouseEvent createEvent(LearningData learningData) {
    return WarehouseEvent.builder()
        .requestId(randomUUID().toString())
        .alerts(singletonList(Alert.builder()
            .alertName(learningData.getAlertName())    //TODO add missing fields
            .build()))
        .build();
  }
}
