package com.silenteight.warehouse.indexer.alert;

import com.silenteight.warehouse.common.opendistro.roles.RolesMappedConstants;

import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.ALERT_PREFIX;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.DISCRIMINATOR;
import static java.util.Map.of;

public class MappedAlertFixtures {

  public static final String MATCH_ID_1_1 = "42df75f8-1ba6-4ce8-93d7-d144ef196011";
  public static final String MATCH_ID_1_2 = "af9f1322-2fb6-416d-a7c1-05575741d012";
  public static final String MATCH_ID_2_1 = "8120f51a-88c5-44c6-b9f9-b5163fa7e021";
  public static final String MATCH_ID_2_2 = "f9fb9828-edb7-441d-b6f9-4d8a10cfc022";

  public static final String DISCRIMINATOR_1 = "457b1498-e348-4a81-8093-6079c1173010";
  public static final String DISCRIMINATOR_2 = "80a8cfc0-86c3-4360-afed-7a1b9a326020";
  public static final String DISCRIMINATOR_3 = "788edd98-af02-49a8-ab75-69cf63397b54";
  public static final String DISCRIMINATOR_4 = "accb9508-3bad-4c5b-825a-8c023e03b7d6";
  public static final String DISCRIMINATOR_5 = "48627744-d573-4edb-89fa-f88a8be4ac11";
  public static final String DISCRIMINATOR_6 = "48627744-d573-4edb-89fa-f88a8be4ac43";
  public static final String DISCRIMINATOR_7 = "8fe9c74e-6485-4045-b715-6bb7ec970d39";

  public static final String DOCUMENT_ID = DISCRIMINATOR_1;
  public static final String DOCUMENT_ID_2 = DISCRIMINATOR_2;
  public static final String DOCUMENT_ID_3 = DISCRIMINATOR_3;
  public static final String DOCUMENT_ID_4 = DISCRIMINATOR_4;
  public static final String DOCUMENT_ID_5 = DISCRIMINATOR_5;
  public static final String DOCUMENT_ID_6 = DISCRIMINATOR_6;
  public static final String SIMULATION_ANALYSIS_ID = "9630b08f-682c-4565-bf4d-c07064c65615";

  public static class SourceAlertKeys {

    public static final String COUNTRY_KEY = "lob_country";
    public static final String RECOMMENDATION_KEY = "recommendation";
    public static final String RISK_TYPE_KEY = "risk_type";
    public static final String STATUS_KEY = "status";
    public static final String ANALYST_DECISION_KEY = "analyst_decision";
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
    public static final String STATUS_KEY = ALERT_PREFIX + SourceAlertKeys.STATUS_KEY;
    public static final String ANALYST_DECISION_KEY =
        ALERT_PREFIX + SourceAlertKeys.ANALYST_DECISION_KEY;
  }

  public static class Values {

    public static final String SOLUTION_EXACT_MATCH = "EXACT_MATCH";
    public static final String SOLUTION_NO_DECISION = "NO_DECISION";
    public static final String SOLUTION_NO_DATA = "NO_DATA";
    public static final String RECOMMENDATION_FP = "FALSE_POSITIVE";
    public static final String RECOMMENDATION_MI = "INVESTIGATE";
    public static final String RISK_TYPE_PEP = "PEP";
    public static final String COUNTRY_UK = "UK";
    public static final String COUNTRY_PL = "PL";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_ERROR = "ERROR";
    public static final String PROCESSING_TIMESTAMP = "2021-04-15T12:17:37.098Z";
    public static final String PROCESSING_TIMESTAMP_2 = "2021-05-05T18:17:37.098Z";
    public static final String PROCESSING_TIMESTAMP_3 = "2021-06-08T13:17:37.098Z";
    public static final String PROCESSING_TIMESTAMP_4 = "2021-06-18T12:10:31.098Z";
    public static final String PROCESSING_TIMESTAMP_5 = "2019-06-18T12:10:31.098Z";
    public static final String PROCESSING_TIMESTAMP_6 = "2020-06-18T12:10:31.098Z";
    public static final String ALERT_NAME = "alerts/123";
    public static final String ANALYST_DECISION_FP = "analyst_decision_false_positive";
    public static final String ANALYST_DECISION_TP = "analyst_decision_true_positive";
    public static final String ANALYST_DECISION_UNKNOWN = "analyst_decision_unknown";
  }

  public static class ResourceName {

    private static final String MATCH_RESOURCE_PREFIX = "matches/";
    public static final String ANALYSIS_RESOURCE_PREFIX = "analysis/";

    static final String MATCH_NAME_1_1 = MATCH_RESOURCE_PREFIX + MATCH_ID_1_1;
    static final String MATCH_NAME_1_2 = MATCH_RESOURCE_PREFIX + MATCH_ID_1_2;
    static final String MATCH_NAME_2_1 = MATCH_RESOURCE_PREFIX + MATCH_ID_2_1;
    static final String MATCH_NAME_2_2 = MATCH_RESOURCE_PREFIX + MATCH_ID_2_2;
    public static final String SIMULATION_ANALYSIS_NAME =
        ANALYSIS_RESOURCE_PREFIX + SIMULATION_ANALYSIS_ID;
  }

  public static final Map<String, Object> MAPPED_ALERT_1 = of(
      DISCRIMINATOR, DISCRIMINATOR_1,
      MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP,
      MappedKeys.COUNTRY_KEY, Values.COUNTRY_UK,
      MappedKeys.STATUS_KEY, Values.STATUS_COMPLETED,
      AlertMapperConstants.INDEX_TIMESTAMP, Values.PROCESSING_TIMESTAMP,
      AlertMapperConstants.ALERT_NAME, Values.ALERT_NAME,
      RolesMappedConstants.COUNTRY_KEY, Values.COUNTRY_UK
  );

  public static final Map<String, Object> MAPPED_ALERT_2 = of(
      DISCRIMINATOR, DISCRIMINATOR_2,
      MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP,
      MappedKeys.COUNTRY_KEY, Values.COUNTRY_UK,
      AlertMapperConstants.INDEX_TIMESTAMP, Values.PROCESSING_TIMESTAMP,
      RolesMappedConstants.COUNTRY_KEY, Values.COUNTRY_UK
  );

  public static final Map<String, Object> MAPPED_ALERT_3 = of(
      DISCRIMINATOR, DISCRIMINATOR_3,
      MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP,
      MappedKeys.COUNTRY_KEY, Values.COUNTRY_UK,
      MappedKeys.RISK_TYPE_KEY, Values.RISK_TYPE_PEP,
      MappedKeys.STATUS_KEY, Values.STATUS_COMPLETED,
      AlertMapperConstants.INDEX_TIMESTAMP, Values.PROCESSING_TIMESTAMP,
      RolesMappedConstants.COUNTRY_KEY, Values.COUNTRY_UK
  );

  public static final Map<String, Object> MAPPED_ALERT_4 = of(
      DISCRIMINATOR, DISCRIMINATOR_4,
      MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP,
      MappedKeys.COUNTRY_KEY, Values.COUNTRY_UK,
      MappedKeys.RISK_TYPE_KEY, Values.RISK_TYPE_PEP,
      MappedKeys.STATUS_KEY, Values.STATUS_COMPLETED,
      AlertMapperConstants.INDEX_TIMESTAMP, Values.PROCESSING_TIMESTAMP_2,
      RolesMappedConstants.COUNTRY_KEY, Values.COUNTRY_UK
  );
  public static final Map<String, Object> MAPPED_ALERT_5 = of(
      DISCRIMINATOR, DISCRIMINATOR_5,
      MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_MI,
      MappedKeys.COUNTRY_KEY, Values.COUNTRY_PL,
      MappedKeys.RISK_TYPE_KEY, Values.RISK_TYPE_PEP,
      MappedKeys.STATUS_KEY, Values.STATUS_COMPLETED,
      AlertMapperConstants.INDEX_TIMESTAMP, Values.PROCESSING_TIMESTAMP_3,
      RolesMappedConstants.COUNTRY_KEY, Values.COUNTRY_PL
  );

  public static final Map<String, Object> MAPPED_ALERT_6 = of(
      DISCRIMINATOR, DISCRIMINATOR_6,
      MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP,
      MappedKeys.COUNTRY_KEY, Values.COUNTRY_PL,
      MappedKeys.RISK_TYPE_KEY, Values.RISK_TYPE_PEP,
      AlertMapperConstants.INDEX_TIMESTAMP, Values.PROCESSING_TIMESTAMP_4,
      RolesMappedConstants.COUNTRY_KEY, Values.COUNTRY_PL
  );

  public static final Map<String, Object> MAPPED_ALERT_7 = of(
      DISCRIMINATOR, DISCRIMINATOR_7,
      MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP,
      MappedKeys.COUNTRY_KEY, Values.COUNTRY_UK,
      MappedKeys.RISK_TYPE_KEY, Values.RISK_TYPE_PEP,
      MappedKeys.STATUS_KEY, Values.STATUS_ERROR,
      AlertMapperConstants.INDEX_TIMESTAMP, Values.PROCESSING_TIMESTAMP,
      RolesMappedConstants.COUNTRY_KEY, Values.COUNTRY_UK
  );

  public static final Map<String, Object> MAPPED_ALERT_8 = of(
      DISCRIMINATOR, DISCRIMINATOR_7,
      MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP,
      MappedKeys.RISK_TYPE_KEY, Values.RISK_TYPE_PEP,
      MappedKeys.ANALYST_DECISION_KEY, Values.ANALYST_DECISION_TP,
      AlertMapperConstants.INDEX_TIMESTAMP, Values.PROCESSING_TIMESTAMP,
      RolesMappedConstants.COUNTRY_KEY, Values.COUNTRY_UK
  );

  public static final Map<String, Object> MAPPED_ALERT_9 = of(
      DISCRIMINATOR, DISCRIMINATOR_7,
      MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP,
      MappedKeys.RISK_TYPE_KEY, Values.RISK_TYPE_PEP,
      MappedKeys.ANALYST_DECISION_KEY, Values.ANALYST_DECISION_FP,
      AlertMapperConstants.INDEX_TIMESTAMP, Values.PROCESSING_TIMESTAMP_4,
      RolesMappedConstants.COUNTRY_KEY, Values.COUNTRY_UK
  );

  public static final Map<String, Object> MAPPED_ALERT_10 = of(
      DISCRIMINATOR, DISCRIMINATOR_7,
      MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP,
      MappedKeys.RISK_TYPE_KEY, Values.RISK_TYPE_PEP,
      MappedKeys.ANALYST_DECISION_KEY, Values.ANALYST_DECISION_UNKNOWN,
      AlertMapperConstants.INDEX_TIMESTAMP, Values.PROCESSING_TIMESTAMP,
      RolesMappedConstants.COUNTRY_KEY, Values.COUNTRY_UK
  );
}
