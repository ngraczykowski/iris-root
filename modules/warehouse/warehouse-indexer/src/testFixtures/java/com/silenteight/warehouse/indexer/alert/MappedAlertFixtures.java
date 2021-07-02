package com.silenteight.warehouse.indexer.alert;

import com.silenteight.warehouse.common.opendistro.roles.RolesMappedConstants;

import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.ALERT_ID_KEY;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.ALERT_PREFIX;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.MATCH_ID_KEY;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.MATCH_PREFIX;

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
  public static final String DOCUMENT_ID_2 = ALERT_ID_1 + ":" + MATCH_ID_1_2;
  public static final String DOCUMENT_ID_3 = ALERT_ID_1 + ":" + MATCH_ID_2_1;
  public static final String DOCUMENT_ID_4 = ALERT_ID_1 + ":" + MATCH_ID_3_1;
  public static final String DOCUMENT_ID_5 = ALERT_ID_4 + ":" + MATCH_ID_4_1;
  public static final String DOCUMENT_ID_6 = ALERT_ID_5 + ":" + MATCH_ID_5_1;
  public static final String SIMULATION_ANALYSIS_ID = "9630b08f-682c-4565-bf4d-c07064c65615";

  public static class SourceAlertKeys {

    public static final String COUNTRY_KEY = "lob_country";
    public static final String RECOMMENDATION_KEY = "recommendation";
    public static final String RISK_TYPE_KEY = "risk_type";
  }

  public static class SourceMatchKeys {

    public static final String SOLUTION_KEY = "solution";
  }

  public static class MappedKeys {

    public static final String RECOMMENDATION_KEY =
        ALERT_PREFIX + SourceAlertKeys.RECOMMENDATION_KEY;
    public static final String RISK_TYPE_KEY =
        ALERT_PREFIX + SourceAlertKeys.RISK_TYPE_KEY;
    public static final String COUNTRY_KEY = ALERT_PREFIX + SourceAlertKeys.COUNTRY_KEY;
    public static final String SOLUTION_KEY = MATCH_PREFIX + SourceMatchKeys.SOLUTION_KEY;
  }

  public static class Values {

    public static final String SOLUTION_EXACT_MATCH = "EXACT_MATCH";
    public static final String SOLUTION_NO_DECISION = "NO_DECISION";
    public static final String SOLUTION_NO_DATA = "NO_DATA";
    public static final String RECOMMENDATION_FP = "FALSE_POSITIVE";
    public static final String RECOMMENDATION_MI = "MANUAL_INVESTIGATION";
    public static final String RISK_TYPE_PEP = "PEP";
    public static final String COUNTRY_UK = "UK";
    public static final String COUNTRY_PL = "PL";
    public static final String PROCESSING_TIMESTAMP = "2021-04-15T12:17:37.098Z";
    public static final String PROCESSING_TIMESTAMP_2 = "2021-05-05T18:17:37.098Z";
    public static final String PROCESSING_TIMESTAMP_3 = "2021-06-08T13:17:37.098Z";
    public static final String PROCESSING_TIMESTAMP_4 = "2021-06-18T12:10:31.098Z";
  }

  public static class ResourceName {

    private static final String MATCH_RESOURCE_PREFIX = "matches/";
    private static final String ALERT_RESOURCE_PREFIX = "alerts/";
    public static final String ANALYSIS_RESOURCE_PREFIX = "analysis/";

    static final String ALERT_NAME_1 = ALERT_RESOURCE_PREFIX + ALERT_ID_1;
    static final String ALERT_NAME_2 = ALERT_RESOURCE_PREFIX + ALERT_ID_2;
    static final String MATCH_NAME_1_1 = MATCH_RESOURCE_PREFIX + MATCH_ID_1_1;
    static final String MATCH_NAME_1_2 = MATCH_RESOURCE_PREFIX + MATCH_ID_1_2;
    static final String MATCH_NAME_2_1 = MATCH_RESOURCE_PREFIX + MATCH_ID_2_1;
    static final String MATCH_NAME_2_2 = MATCH_RESOURCE_PREFIX + MATCH_ID_2_2;
    public static final String SIMULATION_ANALYSIS_NAME =
        ANALYSIS_RESOURCE_PREFIX + SIMULATION_ANALYSIS_ID;
  }

  public static final Map<String, Object> MAPPED_ALERT_WITH_MATCHES_1 = Map.of(
      ALERT_ID_KEY, ALERT_ID_1,
      MATCH_ID_KEY, MATCH_ID_1_1,
      MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP,
      MappedKeys.COUNTRY_KEY, Values.COUNTRY_UK,
      MappedKeys.SOLUTION_KEY, Values.SOLUTION_NO_DECISION,
      AlertMapperConstants.INDEX_TIMESTAMP, Values.PROCESSING_TIMESTAMP,
      RolesMappedConstants.COUNTRY_KEY, Values.COUNTRY_UK
  );

  public static final Map<String, Object> MAPPED_ALERT_WITH_MATCHES_2 = Map.of(
      ALERT_ID_KEY, ALERT_ID_1,
      MATCH_ID_KEY, MATCH_ID_1_2,
      MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP,
      MappedKeys.COUNTRY_KEY, Values.COUNTRY_UK,
      MappedKeys.SOLUTION_KEY, Values.SOLUTION_EXACT_MATCH,
      AlertMapperConstants.INDEX_TIMESTAMP, Values.PROCESSING_TIMESTAMP,
      RolesMappedConstants.COUNTRY_KEY, Values.COUNTRY_UK
  );

  public static final Map<String, Object> MAPPED_ALERT_WITH_MATCHES_3 = Map.of(
      ALERT_ID_KEY, ALERT_ID_2,
      MATCH_ID_KEY, MATCH_ID_1_2,
      MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP,
      MappedKeys.COUNTRY_KEY, Values.COUNTRY_UK,
      MappedKeys.RISK_TYPE_KEY, Values.RISK_TYPE_PEP,
      MappedKeys.SOLUTION_KEY, Values.SOLUTION_EXACT_MATCH,
      AlertMapperConstants.INDEX_TIMESTAMP, Values.PROCESSING_TIMESTAMP,
      RolesMappedConstants.COUNTRY_KEY, Values.COUNTRY_UK
  );

  public static final Map<String, Object> MAPPED_ALERT_WITH_MATCHES_4 = Map.of(
      ALERT_ID_KEY, ALERT_ID_3,
      MATCH_ID_KEY, MATCH_ID_1_2,
      MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP,
      MappedKeys.COUNTRY_KEY, Values.COUNTRY_UK,
      MappedKeys.RISK_TYPE_KEY, Values.RISK_TYPE_PEP,
      MappedKeys.SOLUTION_KEY, Values.SOLUTION_EXACT_MATCH,
      AlertMapperConstants.INDEX_TIMESTAMP, Values.PROCESSING_TIMESTAMP_2,
      RolesMappedConstants.COUNTRY_KEY, Values.COUNTRY_UK
  );
  public static final Map<String, Object> MAPPED_ALERT_WITH_MATCHES_5 = Map.of(
      ALERT_ID_KEY, ALERT_ID_4,
      MATCH_ID_KEY, MATCH_ID_3_1,
      MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_MI,
      MappedKeys.COUNTRY_KEY, Values.COUNTRY_PL,
      MappedKeys.RISK_TYPE_KEY, Values.RISK_TYPE_PEP,
      MappedKeys.SOLUTION_KEY, Values.SOLUTION_EXACT_MATCH,
      AlertMapperConstants.INDEX_TIMESTAMP, Values.PROCESSING_TIMESTAMP_3,
      RolesMappedConstants.COUNTRY_KEY, Values.COUNTRY_PL
  );

  public static final Map<String, Object> MAPPED_ALERT_WITH_MATCHES_6 = Map.of(
      ALERT_ID_KEY, ALERT_ID_5,
      MATCH_ID_KEY, MATCH_ID_5_1,
      MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP,
      MappedKeys.COUNTRY_KEY, Values.COUNTRY_PL,
      MappedKeys.RISK_TYPE_KEY, Values.RISK_TYPE_PEP,
      MappedKeys.SOLUTION_KEY, Values.SOLUTION_EXACT_MATCH,
      AlertMapperConstants.INDEX_TIMESTAMP, Values.PROCESSING_TIMESTAMP_4,
      RolesMappedConstants.COUNTRY_KEY, Values.COUNTRY_PL
  );
}
