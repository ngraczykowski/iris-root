package com.silenteight.warehouse.indexer.alert;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AlertMapperTest {

  static final String ALERT_ID = "44";
  static final String MATCH_ID = "35";

  @InjectMocks
  AlertMapper alertMapper;

  @Test
  void shouldReturnAlertMapWithMatch() {
    //when
    Map<String, Object> preparedMap = alertMapper.convertAlertToAttributes(ALERT_WITH_MATCHES);

    //then
    assertThat(preparedMap).isEqualTo(Map.of(
        AlertMapper.ALERT,
        Map.of(AlertMapper.NAME, ALERT_ID, RECOMMENDATION, FALSE_POSITIVE),
        AlertMapper.MATCH, Map.of(
            AlertMapper.NAME, MATCH_ID, SOLUTION, NO_DECISION
        )
    ));
  }

  @Test
  void shouldReturnAlertMapWithoutMatches() {
    //when
    Map<String, Object> preparedMap =
        alertMapper.convertAlertToAttributes(ALERT_WITHOUT_MATCHES);

    //then
    assertThat(preparedMap).isEqualTo(Map.of(
        AlertMapper.ALERT,
        Map.of(AlertMapper.NAME, ALERT_ID, RECOMMENDATION, MANUAL_INVESTIGATION)));
  }
}
