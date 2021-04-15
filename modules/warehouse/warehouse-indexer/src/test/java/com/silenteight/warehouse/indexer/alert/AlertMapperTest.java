package com.silenteight.warehouse.indexer.alert;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.KEY_ALERT;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.KEY_MATCH;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.KEY_NAME;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.*;
import static java.util.Map.of;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AlertMapperTest {

  @InjectMocks
  AlertMapper alertMapper;

  @Test
  void shouldReturnAlertMapWithMatch() {
    //when
    Map<String, Object> preparedMap = alertMapper.convertAlertToAttributes(ALERT_WITH_MATCHES_1);

    //then
    assertThat(preparedMap).isEqualTo(of(
        KEY_ALERT, of(
            KEY_NAME, ALERT_ID_1,
            ALERT_PAYLOAD_RECOMMENDATION_KEY, ALERT_PAYLOAD_RECOMMENDATION_FP),
        KEY_MATCH, of(
            KEY_NAME, MATCH_ID_1,
            MATCH_PAYLOAD_SOLUTION_KEY, MATCH_PAYLOAD_SOLUTION_NO_DECISION)
    ));
  }

  @Test
  void shouldReturnAlertMapWithoutMatches() {
    //when
    Map<String, Object> preparedMap =
        alertMapper.convertAlertToAttributes(ALERT_WITHOUT_MATCHES);

    //then
    assertThat(preparedMap).isEqualTo(of(
        KEY_ALERT, of(
            KEY_NAME, ALERT_ID_1,
            ALERT_PAYLOAD_RECOMMENDATION_KEY, ALERT_PAYLOAD_RECOMMENDATION_MI)));
  }
}
