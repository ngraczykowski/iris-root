package com.silenteight.warehouse.indexer.alert;

import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.common.opendistro.roles.RolesMappedConstants;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.SourceAlertKeys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_1;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_2;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MAPPED_ALERT_1;
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
    //when
    var preparedMap = alertMapper.convertAlertToAttributes(ALERT_1);

    //then
    assertThat(preparedMap).isEqualTo(MAPPED_ALERT_1);
  }

  @Test
  void shouldNotMapCountryIfNotPresent() {
    var preparedMap = alertMapper.convertAlertToAttributes(ALERT_2);

    assertThat(preparedMap)
        .doesNotContainKey(MappedKeys.COUNTRY_KEY)
        .doesNotContainKey(RolesMappedConstants.COUNTRY_KEY);
  }
}
