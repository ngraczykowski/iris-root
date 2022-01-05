package com.silenteight.simulator.processing.alert.index.domain;

import org.junit.jupiter.api.Test;

import static com.silenteight.simulator.processing.alert.index.domain.IndexedAlertFixtures.createNewIndexAlertEntity;
import static com.silenteight.simulator.processing.alert.index.domain.State.ACKED;
import static com.silenteight.simulator.processing.alert.index.domain.State.SENT;
import static org.assertj.core.api.Assertions.*;

class IndexedAlertEntityTest {

  @Test
  void shouldAck() {
    // given
    IndexedAlertEntity indexedAlert = createNewIndexAlertEntity(SENT);

    // when
    indexedAlert.ack();

    // then
    assertThat(indexedAlert.getState()).isEqualTo(ACKED);
  }
}
