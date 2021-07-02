package com.silenteight.warehouse.indexer.alert;

import com.silenteight.data.api.v1.Match;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.SourceAlertKeys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_WITH_MATCHES_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MAPPED_ALERT_WITH_MATCHES_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP;
import static java.time.Instant.parse;
import static org.assertj.core.api.Assertions.*;

class AlertMapperTest {

  AlertMapper alertMapper;

  @BeforeEach
  public void init() {
    AlertMappingProperties alertMappingProperties = new AlertMappingProperties();
    alertMappingProperties.setCountrySourceKey(SourceAlertKeys.COUNTRY_KEY);

    MockTimeSource mockTimeSource = new MockTimeSource(parse(PROCESSING_TIMESTAMP));

    alertMapper = new AlertMapper(mockTimeSource, alertMappingProperties);
  }

  @Test
  void shouldReturnAlertMapWithMatch() {
    //given
    int firstMatch = 0;
    Match match = ALERT_WITH_MATCHES_1.getMatches(firstMatch);

    //when
    var preparedMap = alertMapper.convertAlertAndMatchToAttributes(ALERT_WITH_MATCHES_1, match);

    //then
    assertThat(preparedMap).isEqualTo(MAPPED_ALERT_WITH_MATCHES_1);
  }
}
