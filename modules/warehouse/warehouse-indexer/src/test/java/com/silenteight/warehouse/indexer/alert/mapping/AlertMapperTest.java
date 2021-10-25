package com.silenteight.warehouse.indexer.alert.mapping;

import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.SourceAlertKeys;
import com.silenteight.warehouse.indexer.support.PayloadConverter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.silenteight.warehouse.indexer.IndexerFixtures.ALERT_DEFINITION_1;
import static com.silenteight.warehouse.indexer.IndexerFixtures.EMPTY_PAYLOAD;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DISCRIMINATOR_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MAPPED_ALERT_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP;
import static java.time.Instant.parse;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;

class AlertMapperTest {

  private AlertMapper underTest;

  @BeforeEach
  public void init() {
    AlertMappingProperties alertMappingProperties = new AlertMappingProperties();
    alertMappingProperties.setCountrySourceKey(SourceAlertKeys.COUNTRY_KEY);

    MockTimeSource mockTimeSource = new MockTimeSource(parse(PROCESSING_TIMESTAMP));
    PayloadConverter payloadConverter = new PayloadConverter(emptyList());

    underTest = new AlertMapper(mockTimeSource, alertMappingProperties, payloadConverter);
  }

  @Test
  void shouldReturnAlertMapWithMatch() {
    //when
    var preparedMap = underTest.convertAlertToAttributes(ALERT_DEFINITION_1);

    //then
    assertThat(preparedMap).isEqualTo(MAPPED_ALERT_1);
  }

  @Test
  void shouldNotMapAnyOptionalFields() {
    var preparedMap = underTest.convertAlertToAttributes(
        AlertDefinition.builder()
            .discriminator(DISCRIMINATOR_1)
            .payload(EMPTY_PAYLOAD)
            .build());

    assertThat(preparedMap).containsOnlyKeys(
        AlertMapperConstants.INDEX_TIMESTAMP,
        AlertMapperConstants.DISCRIMINATOR);
  }
}
