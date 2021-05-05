package com.silenteight.warehouse.indexer.alert;

import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.*;

public class MappedAlertFixtures {

  public static final String MATCH_ID_1 = "35";
  public static final String MATCH_ID_2 = "36";
  public static final String ALERT_ID_1 = "44";
  public static final String ALERT_ID_2 = "45";
  public static final String ANALYSIS_ID = "33";
  public static final String DOCUMENT_ID = "44:35";
  public static final String MATCH_PAYLOAD_SOLUTION_KEY = "solution";
  public static final String MATCH_PAYLOAD_SOLUTION_EXACT_MATCH = "EXACT_MATCH";
  public static final String MATCH_PAYLOAD_SOLUTION_NO_DECISION = "NO_DECISION";
  public static final String ALERT_PAYLOAD_RECOMMENDATION_KEY = "recommendation";
  public static final String ALERT_PAYLOAD_RECOMMENDATION_FP = "FALSE_POSITIVE";
  public static final String ALERT_PAYLOAD_RECOMMENDATION_MI = "MANUAL_INVESTIGATION";
  public static final String PROCESSING_TIMESTAMP = "2021-04-15T12:17:37.098Z";

  private static final String MATCH_RESOURCE_PREFIX = "matches/";
  private static final String ALERT_RESOURCE_PREFIX = "alerts/";
  private static final String ANALYSIS_RESOURCE_PREFIX = "analysis/";

  static final String REQUEST_ID = "TEST_123";
  static final String NO_DATA = "NO_DATA";
  static final String MATCH_NAME_1 = MATCH_RESOURCE_PREFIX + MATCH_ID_1;
  static final String MATCH_NAME_2 = MATCH_RESOURCE_PREFIX + MATCH_ID_2;
  static final String ALERT_NAME_1 = ALERT_RESOURCE_PREFIX + ALERT_ID_1;
  static final String ALERT_NAME_2 = ALERT_RESOURCE_PREFIX + ALERT_ID_2;
  static final String ANALYSIS_NAME = ANALYSIS_RESOURCE_PREFIX + ANALYSIS_ID;

  public static final Map<String, Object> ALERT_WITH_MATCHES_1_MAP = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP,
      ALERT_ID_KEY, ALERT_ID_1,
      ALERT_PREFIX + ALERT_PAYLOAD_RECOMMENDATION_KEY, ALERT_PAYLOAD_RECOMMENDATION_FP,
      MATCH_ID_KEY, MATCH_ID_1,
      MATCH_PREFIX + MATCH_PAYLOAD_SOLUTION_KEY, MATCH_PAYLOAD_SOLUTION_NO_DECISION
  );
}
