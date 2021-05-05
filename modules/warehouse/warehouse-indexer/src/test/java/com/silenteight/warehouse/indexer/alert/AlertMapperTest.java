package com.silenteight.warehouse.indexer.alert;

import com.silenteight.data.api.v1.Match;
import com.silenteight.sep.base.testing.time.MockTimeSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_WITH_MATCHES_1;
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
    //given
    int firstMatch = 0;
    Match match = ALERT_WITH_MATCHES_1.getMatches(firstMatch);

    //when
    var preparedMap = alertMapper.convertAlertAndMatchToAttributes(ALERT_WITH_MATCHES_1, match);

    //then
    assertThat(preparedMap).isEqualTo(ALERT_WITH_MATCHES_1_MAP);
  }
}
