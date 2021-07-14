package com.silenteight.warehouse.indexer.alert;

import com.silenteight.warehouse.indexer.alert.AlertsAttributesListDto.AlertAttributes;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys;

import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.DISCRIMINATOR;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.RECOMMENDATION_FP;
import static java.util.List.of;

class AlertControllerConstants {

  static final String DISCRIMINATOR_ID = "2Fc01e5d49-a72d-4e6a-b634-265165916c9a";

  static final String QA_ALERT_LIST_URL =
      "/v1/analysis/production/query?fields=s8_discriminator,riskType,s8_country"
          + "&name=c01e5d49-a72d-4e6a-b634-265165916c9a,"
          + "5d49c01e-4e6a-a72d-34b6-916c9a265165";
  static final String QA_ALERT_URL =
      "/v1/analysis/production/alerts/c01e5d49-a72d-4e6a-b634-265165916c9a"
          + "?fields=s8_discriminator,alert_recommendation";

  static final AlertAttributes ALERT_ATTRIBUTES = AlertAttributes.builder()
      .attributes(Map.of(
          DISCRIMINATOR, DISCRIMINATOR_ID,
          MappedKeys.RECOMMENDATION_KEY, RECOMMENDATION_FP))
      .build();

  static final AlertsAttributesListDto ALERT_ATTRIBUTES_LIST_DTO = AlertsAttributesListDto.builder()
      .alerts(of(ALERT_ATTRIBUTES))
      .build();
}
