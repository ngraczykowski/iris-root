package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.LearningData;
import com.silenteight.fab.dataprep.domain.model.WarehouseEvent;
import com.silenteight.fab.dataprep.domain.model.WarehouseEvent.Alert;
import com.silenteight.fab.dataprep.domain.outgoing.LearningEventPublisher;

import org.springframework.stereotype.Service;

import java.util.Map;

import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class LearningService {

  private static final String PREFIX = "warehouse_alert.payload -> ";
  private static final String ANALYST_DECISION = PREFIX + "analystDecision";
  private static final String ORIGINAL_ANALYST_DECISION = PREFIX + "originalAnalystDecision";
  private static final String ANALYST_DECISION_MODIFIED_DATE_TIME =
      PREFIX + "analystDecisionModifiedDateTime";
  private static final String ANALYST_REASON = PREFIX + "analystReason";

  private final LearningEventPublisher learningEventPublisher;

  public void feedWarehouse(LearningData learningData) {
    WarehouseEvent warehouseEvent = createEvent(learningData);

    learningEventPublisher.publish(warehouseEvent);
  }

  private static WarehouseEvent createEvent(LearningData learningData) {
    return WarehouseEvent.builder()
        .requestId(randomUUID().toString())
        .alerts(singletonList(Alert.builder()
            .alertName(learningData.getAlertName())
            .discriminator(learningData.getDiscriminator())
            .accessPermissionTag(learningData.getAccessPermissionTag())
            .payload(createPayload(learningData))
            .build()))
        .build();
  }

  private static Map<String, String> createPayload(LearningData learningData) {
    return Map.of(ANALYST_DECISION, learningData.getAnalystDecision(),
        ORIGINAL_ANALYST_DECISION, learningData.getOriginalAnalystDecision(),
        ANALYST_DECISION_MODIFIED_DATE_TIME,
        learningData.getAnalystDecisionModifiedDateTime(),
        ANALYST_REASON, learningData.getAnalystReason());
  }
}
