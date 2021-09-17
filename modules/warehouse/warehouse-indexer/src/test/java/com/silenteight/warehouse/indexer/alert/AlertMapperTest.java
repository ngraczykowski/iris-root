package com.silenteight.warehouse.indexer.alert;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.SourceAlertKeys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DISCRIMINATOR_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MAPPED_ALERT_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP;
import static java.time.Instant.parse;
import static org.assertj.core.api.Assertions.*;

class AlertMapperTest {

  private static final Predicate<String> DO_NOT_SKIP_KEYS = key -> true;
  private static final Predicate<String> SKIP_ALL_GENERIC_KEYS = key -> false;

  private AlertMapper underTest;

  @BeforeEach
  public void init() {
    AlertMappingProperties alertMappingProperties = new AlertMappingProperties();
    alertMappingProperties.setCountrySourceKey(SourceAlertKeys.COUNTRY_KEY);

    MockTimeSource mockTimeSource = new MockTimeSource(parse(PROCESSING_TIMESTAMP));

    underTest = new AlertMapper(mockTimeSource, alertMappingProperties, DO_NOT_SKIP_KEYS);
  }

  @Test
  void shouldReturnAlertMapWithMatch() {
    //when
    var preparedMap = underTest.convertAlertToAttributes(ALERT_1);

    //then
    assertThat(preparedMap).isEqualTo(MAPPED_ALERT_1);
  }

  @Test
  void shouldNotMapAnyOptionalFields() {
    var preparedMap = underTest.convertAlertToAttributes(Alert.newBuilder()
        .setDiscriminator(DISCRIMINATOR_1)
        .build());

    assertThat(preparedMap).containsOnlyKeys(
        AlertMapperConstants.INDEX_TIMESTAMP,
        AlertMapperConstants.DISCRIMINATOR);
  }

  @Test
  void shouldSkipAllFields() {
    AlertMappingProperties alertMappingProperties = new AlertMappingProperties();
    alertMappingProperties.setCountrySourceKey(SourceAlertKeys.COUNTRY_KEY);
    MockTimeSource mockTimeSource = new MockTimeSource(parse(PROCESSING_TIMESTAMP));

    underTest = new AlertMapper(mockTimeSource, alertMappingProperties, SKIP_ALL_GENERIC_KEYS);
    var preparedMap = underTest.convertAlertToAttributes(ALERT_1);

    assertThat(preparedMap)
        .doesNotContainKey(MappedKeys.RECOMMENDATION_KEY)
        .doesNotContainKey(MappedKeys.COUNTRY_KEY);
  }
}
