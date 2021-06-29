package com.silenteight.warehouse.indexer.alert;

import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.*;
import static java.util.List.of;

public class MappedAlertFixtures {

  public static final String MATCH_ID_1_1 = "42df75f8-1ba6-4ce8-93d7-d144ef196011";
  public static final String MATCH_ID_1_2 = "af9f1322-2fb6-416d-a7c1-05575741d012";
  public static final String MATCH_ID_2_1 = "8120f51a-88c5-44c6-b9f9-b5163fa7e021";
  public static final String MATCH_ID_2_2 = "f9fb9828-edb7-441d-b6f9-4d8a10cfc022";
  public static final String MATCH_ID_3_1 = "f9fb9828-edb7-441d-b6f9-4d8a10cfc022";
  public static final String MATCH_ID_4_1 = "10a88ebe-7eae-4389-b5ba-996e80aa0e38";
  public static final String MATCH_ID_5_1 = "e59ad2e8-8efb-4d5a-8eda-9516108af709";
  public static final String ALERT_ID_1 = "457b1498-e348-4a81-8093-6079c1173010";
  public static final String ALERT_ID_2 = "80a8cfc0-86c3-4360-afed-7a1b9a326020";
  public static final String ALERT_ID_3 = "788edd98-af02-49a8-ab75-69cf63397b54";
  public static final String ALERT_ID_4 = "accb9508-3bad-4c5b-825a-8c023e03b7d6";
  public static final String ALERT_ID_5 = "48627744-d573-4edb-89fa-f88a8be4ac11";
  public static final String DOCUMENT_ID = ALERT_ID_1 + ":" + MATCH_ID_1_1;
  public static final String MATCH_SOLUTION_KEY = "solution";
  public static final String MATCH_SOLUTION_EXACT_MATCH = "EXACT_MATCH";
  public static final String MATCH_SOLUTION_NO_DECISION = "NO_DECISION";
  public static final String ALERT_RECOMMENDATION_KEY = "recommendation";
  public static final String ALERT_RECOMMENDATION_FP = "FALSE_POSITIVE";
  public static final String ALERT_RECOMMENDATION_MI = "MANUAL_INVESTIGATION";
  public static final String ALERT_RISK_TYPE_PEP = "PEP";
  public static final String ALERT_RISK_TYPE_KEY = "riskType";
  public static final String ALERT_COUNTRY_KEY = "country";
  public static final String ALERT_COUNTRY_UK = "UK";
  public static final String ALERT_COUNTRY_PL = "PL";
  public static final String PROCESSING_TIMESTAMP_1 = "2021-04-15T12:17:37.098Z";
  public static final String PROCESSING_TIMESTAMP_2 = "2021-05-05T18:17:37.098Z";
  public static final String PROCESSING_TIMESTAMP_3 = "2021-06-08T13:17:37.098Z";
  public static final String PROCESSING_TIMESTAMP_4 = "2021-06-18T12:10:31.098Z";
  public static final String DOCUMENT_ID_1 = ALERT_ID_1 + ":" + MATCH_ID_1_1;
  public static final String DOCUMENT_ID_2 = ALERT_ID_1 + ":" + MATCH_ID_1_2;
  public static final String DOCUMENT_ID_3 = ALERT_ID_1 + ":" + MATCH_ID_2_1;
  public static final String DOCUMENT_ID_4 = ALERT_ID_1 + ":" + MATCH_ID_3_1;
  public static final String DOCUMENT_ID_5 = ALERT_ID_4 + ":" + MATCH_ID_4_1;
  public static final String DOCUMENT_ID_6 = ALERT_ID_5 + ":" + MATCH_ID_5_1;
  public static final String SIMULATION_ANALYSIS_ID = "9630b08f-682c-4565-bf4d-c07064c65615";
  public static final List<String> ALERT_FIELDS =
      of(ALERT_RECOMMENDATION_KEY, ALERT_RISK_TYPE_KEY, ALERT_COUNTRY_KEY);
  public static final List<String> ALERT_FIELDS_WITH_ONE_NON_EXISTING =
      of(ALERT_RECOMMENDATION_KEY, ALERT_RISK_TYPE_KEY);
  public static final List<String> LIST_OF_ID =
      of(
          "alerts/457b1498-e348-4a81-8093-6079c1173010",
          "alerts/80a8cfc0-86c3-4360-afed-7a1b9a326020");

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

  static final List<String> LIST_OF_MATCHED_ALERT_ID =
      of(ALERT_ID_1, ALERT_ID_2, ALERT_ID_3);

  public static final Map<String, Object> ALERT_WITH_MATCHES_1_MAP = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP_1,
      ALERT_ID_KEY, ALERT_ID_1,
      ALERT_PREFIX + ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_FP,
      ALERT_PREFIX + ALERT_COUNTRY_KEY, ALERT_COUNTRY_UK,
      MATCH_ID_KEY, MATCH_ID_1_1,
      MATCH_PREFIX + MATCH_SOLUTION_KEY, MATCH_SOLUTION_NO_DECISION
  );

  public static final Map<String, Object> ALERT_WITH_MATCHES_2_MAP = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP_1,
      ALERT_ID_KEY, ALERT_ID_1,
      ALERT_PREFIX + ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_FP,
      ALERT_PREFIX + ALERT_COUNTRY_KEY, ALERT_COUNTRY_UK,
      MATCH_ID_KEY, MATCH_ID_1_2,
      MATCH_PREFIX + MATCH_SOLUTION_KEY, MATCH_SOLUTION_EXACT_MATCH
  );

  public static final Map<String, Object> ALERT_WITH_MATCHES_3_MAP = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP_1,
      ALERT_ID_KEY, ALERT_ID_2,
      ALERT_PREFIX + ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_FP,
      ALERT_PREFIX + ALERT_COUNTRY_KEY, ALERT_COUNTRY_UK,
      ALERT_PREFIX + ALERT_RISK_TYPE_KEY, ALERT_RISK_TYPE_PEP,
      MATCH_ID_KEY, MATCH_ID_1_2,
      MATCH_PREFIX + MATCH_SOLUTION_KEY, MATCH_SOLUTION_EXACT_MATCH
  );
  public static final Map<String, Object> ALERT_WITH_MATCHES_4_MAP = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP_2,
      ALERT_ID_KEY, ALERT_ID_3,
      ALERT_PREFIX + ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_FP,
      ALERT_PREFIX + ALERT_COUNTRY_KEY, ALERT_COUNTRY_UK,
      ALERT_PREFIX + ALERT_RISK_TYPE_KEY, ALERT_RISK_TYPE_PEP,
      MATCH_ID_KEY, MATCH_ID_1_2,
      MATCH_PREFIX + MATCH_SOLUTION_KEY, MATCH_SOLUTION_EXACT_MATCH
  );
  public static final Map<String, Object> ALERT_WITH_MATCHES_5_MAP = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP_3,
      ALERT_ID_KEY, ALERT_ID_4,
      ALERT_PREFIX + ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_MI,
      ALERT_PREFIX + ALERT_COUNTRY_KEY, ALERT_COUNTRY_PL,
      ALERT_PREFIX + ALERT_RISK_TYPE_KEY, ALERT_RISK_TYPE_PEP,
      MATCH_ID_KEY, MATCH_ID_3_1,
      MATCH_PREFIX + MATCH_SOLUTION_KEY, MATCH_SOLUTION_EXACT_MATCH
  );

  public static final Map<String, Object> ALERT_WITH_MATCHES_6_MAP = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP_4,
      ALERT_ID_KEY, ALERT_ID_5,
      ALERT_PREFIX + ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_FP,
      ALERT_PREFIX + ALERT_COUNTRY_KEY, ALERT_COUNTRY_PL,
      ALERT_PREFIX + ALERT_RISK_TYPE_KEY, ALERT_RISK_TYPE_PEP,
      MATCH_ID_KEY, MATCH_ID_5_1,
      MATCH_PREFIX + MATCH_SOLUTION_KEY, MATCH_SOLUTION_EXACT_MATCH
  );
}
