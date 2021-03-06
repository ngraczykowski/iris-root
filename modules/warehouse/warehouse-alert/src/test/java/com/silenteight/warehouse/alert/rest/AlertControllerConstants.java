package com.silenteight.warehouse.alert.rest;

import com.silenteight.warehouse.indexer.query.dto.AlertDetailsRequest;

import java.util.Collection;
import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys.RECOMMENDATION_KEY;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.RECOMMENDATION_FP;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.ALERT_NAME;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.DISCRIMINATOR;
import static java.util.List.of;

class AlertControllerConstants {

  static final String DISCRIMINATOR_ID = "2Fc01e5d49-a72d-4e6a-b634-265165916c9a";
  static final String ALERT_NAME_1 = "alerts/2Fc01e5d49-a72d-4e6a-b634-265165916c9a";

  static final String QA_ALERT_LIST_URL =
      "/v1/analysis/production/query?fields=s8_discriminator,riskType,s8_country"
          + "&name=c01e5d49-a72d-4e6a-b634-265165916c9a,"
          + "5d49c01e-4e6a-a72d-34b6-916c9a265165";
  static final String QA_ALERT_URL =
      "/v1/analysis/production/alerts/c01e5d49-a72d-4e6a-b634-265165916c9a"
          + "?fields=s8_discriminator,alert_recommendation";

  static final AlertDetailsRequest QA_ALERT_DETAILS_BODY = AlertDetailsRequest
      .builder()
      .fields(of("s8_discriminator", "alert_recommendation", "s8_alert_name"))
      .build();

  static final Map<String, String> ALERT_ATTRIBUTES = Map.of(
      DISCRIMINATOR, DISCRIMINATOR_ID,
      RECOMMENDATION_KEY, RECOMMENDATION_FP,
      ALERT_NAME, ALERT_NAME_1);

  static final Collection<Map<String, String>> ALERT_ATTRIBUTES_LIST = of(ALERT_ATTRIBUTES);
}
