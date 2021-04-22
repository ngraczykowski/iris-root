package com.silenteight.warehouse.indexer.alert;

import com.silenteight.sep.base.testing.time.MockTimeSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_WITHOUT_MATCHES;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_WITH_MATCHES_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_WITHOUT_MATCHES_MAP;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_WITH_MATCHES_1_MAP;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.PROCESSING_TIMESTAMP;
import static java.time.Instant.parse;
import static org.assertj.core.api.Assertions.*;

class AlertMapperTest {

  AlertMapper alertMapper;

  @BeforeEach
  public void init() {
    alertMapper = new AlertMapper(new MockTimeSource(parse(PROCESSING_TIMESTAMP)));
  }

  @Test
  void shouldReturnAlertMapWithMatch() {
    //when
    var preparedMap = alertMapper.convertAlertToAttributes(ALERT_WITH_MATCHES_1);

    //then
    assertThat(preparedMap).isEqualTo(ALERT_WITH_MATCHES_1_MAP);
  }

  @Test
  void shouldReturnAlertMapWithoutMatches() {
    //when
    var preparedMap = alertMapper.convertAlertToAttributes(ALERT_WITHOUT_MATCHES);

    //then
    assertThat(preparedMap).isEqualTo(ALERT_WITHOUT_MATCHES_MAP);
  }
}
