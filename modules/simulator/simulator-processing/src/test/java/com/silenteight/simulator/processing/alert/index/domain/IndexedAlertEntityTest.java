package com.silenteight.simulator.processing.alert.index.domain;

import org.junit.jupiter.api.Test;

import static com.silenteight.simulator.processing.alert.index.domain.IndexedAlertFixtures.ALERT_COUNT;
import static com.silenteight.simulator.processing.alert.index.domain.IndexedAlertFixtures.ANALYSIS_NAME;
import static com.silenteight.simulator.processing.alert.index.domain.IndexedAlertFixtures.REQUEST_ID;
import static com.silenteight.simulator.processing.alert.index.domain.State.ACKED;
import static com.silenteight.simulator.processing.alert.index.domain.State.SENT;
import static org.assertj.core.api.Assertions.*;

class IndexedAlertEntityTest {

  @Test
  void shouldAck() {
    // given
    IndexedAlertEntity indexedAlert = createIndexedAlert();

    // when
    indexedAlert.ack();

    // then
    assertThat(indexedAlert.getState()).isEqualTo(ACKED);
  }

  private static IndexedAlertEntity createIndexedAlert() {
    return IndexedAlertEntity.builder()
        .requestId(REQUEST_ID)
        .analysisName(ANALYSIS_NAME)
        .alertCount(ALERT_COUNT)
        .state(SENT)
        .build();
  }
}
