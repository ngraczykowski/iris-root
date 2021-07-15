package com.silenteight.simulator.processing.alert.index.ack;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.simulator.processing.alert.index.dto.IndexedAlertDto;

import static com.silenteight.adjudication.api.v1.Analysis.State.DONE;
import static com.silenteight.simulator.processing.alert.index.domain.State.ACKED;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class AckMessageFixtures {

  static final String REQUEST_ID = "b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  static final String ANALYSIS_NAME = "Analysis name";

  static final DataIndexResponse INDEX_RESPONSE = DataIndexResponse
      .newBuilder()
      .setRequestId(REQUEST_ID)
      .build();

  static final Analysis ANALYSIS = Analysis
      .newBuilder()
      .setState(DONE)
      .build();

  static final IndexedAlertDto INDEXED_ALERT_DTO = IndexedAlertDto
      .builder()
      .state(ACKED)
      .alertCount(5L)
      .analysisName(ANALYSIS_NAME)
      .requestId(REQUEST_ID)
      .build();
}
