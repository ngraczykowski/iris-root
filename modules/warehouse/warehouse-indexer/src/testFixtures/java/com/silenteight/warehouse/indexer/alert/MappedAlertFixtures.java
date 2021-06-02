package com.silenteight.warehouse.indexer.alert;

import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.*;

public class MappedAlertFixtures {

  public static final String MATCH_ID_1_1 = "42df75f8-1ba6-4ce8-93d7-d144ef196011";
  public static final String MATCH_ID_1_2 = "af9f1322-2fb6-416d-a7c1-05575741d012";
  public static final String MATCH_ID_2_1 = "8120f51a-88c5-44c6-b9f9-b5163fa7e021";
  public static final String MATCH_ID_2_2 = "f9fb9828-edb7-441d-b6f9-4d8a10cfc022";
  public static final String ALERT_ID_1 = "457b1498-e348-4a81-8093-6079c1173010";
  public static final String ALERT_ID_2 = "80a8cfc0-86c3-4360-afed-7a1b9a326020";
  public static final String DOCUMENT_ID = ALERT_ID_1 + ":" + MATCH_ID_1_1;
  public static final String MATCH_PAYLOAD_SOLUTION_KEY = "solution";
  public static final String MATCH_PAYLOAD_SOLUTION_EXACT_MATCH = "EXACT_MATCH";
  public static final String MATCH_PAYLOAD_SOLUTION_NO_DECISION = "NO_DECISION";
  public static final String ALERT_PAYLOAD_RECOMMENDATION_KEY = "recommendation";
  public static final String ALERT_PAYLOAD_RECOMMENDATION_FP = "FALSE_POSITIVE";
  public static final String ALERT_PAYLOAD_RECOMMENDATION_MI = "MANUAL_INVESTIGATION";
  public static final String PROCESSING_TIMESTAMP = "2021-04-15T12:17:37.098Z";

  private static final String MATCH_RESOURCE_PREFIX = "matches/";
  private static final String ALERT_RESOURCE_PREFIX = "alerts/";
  public static final String ANALYSIS_RESOURCE_PREFIX = "analysis/";

  static final String NO_DATA = "NO_DATA";
  static final String MATCH_NAME_1_1 = MATCH_RESOURCE_PREFIX + MATCH_ID_1_1;
  static final String MATCH_NAME_1_2 = MATCH_RESOURCE_PREFIX + MATCH_ID_1_2;
  static final String MATCH_NAME_2_1 = MATCH_RESOURCE_PREFIX + MATCH_ID_2_1;
  static final String MATCH_NAME_2_2 = MATCH_RESOURCE_PREFIX + MATCH_ID_2_2;
  static final String ALERT_NAME_1 = ALERT_RESOURCE_PREFIX + ALERT_ID_1;
  static final String ALERT_NAME_2 = ALERT_RESOURCE_PREFIX + ALERT_ID_2;

  public static final Map<String, Object> ALERT_WITH_MATCHES_1_MAP = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP,
      ALERT_ID_KEY, ALERT_ID_1,
      ALERT_PREFIX + ALERT_PAYLOAD_RECOMMENDATION_KEY, ALERT_PAYLOAD_RECOMMENDATION_FP,
      MATCH_ID_KEY, MATCH_ID_1_1,
      MATCH_PREFIX + MATCH_PAYLOAD_SOLUTION_KEY, MATCH_PAYLOAD_SOLUTION_NO_DECISION
  );
}
