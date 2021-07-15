package com.silenteight.simulator.processing.alert.index.domain;

import static com.silenteight.simulator.processing.alert.index.domain.State.ACKED;
import static com.silenteight.simulator.processing.alert.index.domain.State.SENT;

class IndexedAlertFixtures {

  static final String REQUEST_ID = "b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  static final String REQUEST_ID_2 = "b4708d8c-4832-6fde-9999-d17b4708d8ca";
  static final String ANALYSIS_NAME = "analysis/04e81eda-5ce7-4ce7-843c-34ee32a5182f";
  static final long ALERT_COUNT = 5L;

  static final IndexedAlertEntity SENT_INDEXED_ALERT_ENTITY = IndexedAlertEntity.builder()
      .requestId(REQUEST_ID)
      .analysisName(ANALYSIS_NAME)
      .alertCount(ALERT_COUNT)
      .state(SENT)
      .build();

  static final IndexedAlertEntity ACKED_INDEXED_ALERT_ENTITY = IndexedAlertEntity.builder()
      .requestId(REQUEST_ID_2)
      .analysisName(ANALYSIS_NAME)
      .alertCount(ALERT_COUNT)
      .state(ACKED)
      .build();
}
