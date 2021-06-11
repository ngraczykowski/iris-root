package com.silenteight.warehouse.indexer.alert;

import com.silenteight.warehouse.indexer.alert.AlertsAttributesListDto.AlertAttributes;

import java.util.Map;

import static java.util.List.of;

class AlertControllerConstants {

  static final String ALERT_ID = "2Fc01e5d49-a72d-4e6a-b634-265165916c9a";

  static final String QA_ALERT_LIST_URL =
      "/v1/analysis/production/query?fields=alertId,alertName,riskType,country"
          + "&name=alerts%2Fc01e5d49-a72d-4e6a-b634-265165916c9a,"
          + "alerts%2F5d49c01e-4e6a-a72d-34b6-916c9a265165";
  static final String QA_ALERT_URL =
      "/v1/analysis/production/alerts/c01e5d49-a72d-4e6a-b634-265165916c9a"
          + "?fields=alert_id,alert_recommendation";

  static final AlertAttributes ALERT_ATTRIBUTES = AlertAttributes.builder()
      .attributes(
          Map.of("alertId", ALERT_ID, "alert_recommendation",
              "FALSE_POSITIVE")).build();

  static final AlertsAttributesListDto ALERT_ATTRIBUTES_LIST_DTO = AlertsAttributesListDto.builder()
      .alerts(of(ALERT_ATTRIBUTES))
      .build();
}
