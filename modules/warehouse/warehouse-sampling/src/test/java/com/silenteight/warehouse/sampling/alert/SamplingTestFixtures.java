package com.silenteight.warehouse.sampling.alert;

import com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleRequest;
import com.silenteight.model.api.v1.SampleAlertServiceProto.RequestedAlertsFilter;

import com.google.protobuf.Timestamp;

import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.ALERT_NAME;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.ALERT_PREFIX;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.DISCRIMINATOR;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.INDEX_TIMESTAMP;

public class SamplingTestFixtures {

  public static final String DISCRIMINATOR_1 = "457b1498-e348-4a81-8093-6079c1173010";
  public static final String DISCRIMINATOR_2 = "80a8cfc0-86c3-4360-afed-7a1b9a326020";
  public static final String DISCRIMINATOR_3 = "788edd98-af02-49a8-ab75-69cf63397b54";
  public static final String DISCRIMINATOR_4 = "accb9508-3bad-4c5b-825a-8c023e03b7d6";
  public static final String DISCRIMINATOR_5 = "48627744-d573-4edb-89fa-f88a8be4ac11";
  public static final String DISCRIMINATOR_6 = "e75ae6a7-f0c8-4bc4-b09e-1a48b401e6e5";
  public static final String DISCRIMINATOR_7 = "e75ae6a7-f0c8-4edb-b09e-1a48b401e6e5";
  public static final String ALERT_NAME_1 = "457b1498-e348-4a81-8093-6079c1173010";
  public static final String ALERT_NAME_2 = "80a8cfc0-86c3-4360-afed-7a1b9a326020";
  public static final String ALERT_NAME_3 = "788edd98-af02-49a8-ab75-69cf63397b54";
  public static final String ALERT_NAME_4 = "accb9508-3bad-4c5b-825a-8c023e03b7d6";
  public static final String ALERT_NAME_5 = "48627744-d573-4edb-89fa-f88a8be4ac11";
  public static final String ALERT_NAME_6 = "e75ae6a7-f0c8-4bc4-b09e-1a48b401e6e5";
  public static final String ALERT_RECOMMENDATION_KEY = "recommendation";
  public static final String ALERT_RECOMMENDATION_FP = "FALSE_POSITIVE";
  public static final String ALERT_RECOMMENDATION_MI = "INVESTIGATE";
  public static final String ALERT_RISK_TYPE_PEP = "PEP";
  public static final String ALERT_RISK_TYPE_SAN = "SAN";
  public static final String ALERT_RISK_TYPE_KEY = "riskType";
  public static final String ALERT_COUNTRY_KEY = "country";
  public static final String ALERT_COUNTRY_UK = "UK";
  public static final String ALERT_COUNTRY_PL = "PL";
  public static final String ALERT_STATUS_KEY = "status";
  public static final String ALERT_STATUS_COMPLETED = "COMPLETED";
  public static final String ALERT_STATUS_LEARNING_COMPLETED = "LEARNING_COMPLETED";
  public static final String ALERT_STATUS_ERROR = "ERROR";
  public static final String PROCESSING_TIMESTAMP_1 = "2021-04-15T12:17:37.098Z";
  public static final String PROCESSING_TIMESTAMP_2 = "2021-05-05T18:17:37.098Z";
  public static final String PROCESSING_TIMESTAMP_3 = "2021-06-08T13:17:37.098Z";
  public static final String PROCESSING_TIMESTAMP_4 = "2021-06-18T12:10:31.098Z";
  public static final String DOCUMENT_ID_1 = DISCRIMINATOR_1;
  public static final String DOCUMENT_ID_2 = DISCRIMINATOR_2;
  public static final String DOCUMENT_ID_3 = DISCRIMINATOR_3;
  public static final String DOCUMENT_ID_4 = DISCRIMINATOR_4;
  public static final String DOCUMENT_ID_5 = DISCRIMINATOR_5;
  public static final String DOCUMENT_ID_6 = DISCRIMINATOR_6;
  public static final Map<String, Object> ALERT_1_MAP = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP_1,
      DISCRIMINATOR, DISCRIMINATOR_1,
      ALERT_NAME, ALERT_NAME_1,
      ALERT_PREFIX + ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_FP,
      ALERT_PREFIX + ALERT_COUNTRY_KEY, ALERT_COUNTRY_UK,
      ALERT_PREFIX + ALERT_STATUS_KEY, ALERT_STATUS_COMPLETED
  );
  public static final Map<String, Object> ALERT_2_MAP = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP_1,
      DISCRIMINATOR, DISCRIMINATOR_2,
      ALERT_NAME, ALERT_NAME_2,
      ALERT_PREFIX + ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_FP,
      ALERT_PREFIX + ALERT_COUNTRY_KEY, ALERT_COUNTRY_UK,
      ALERT_PREFIX + ALERT_RISK_TYPE_KEY, ALERT_RISK_TYPE_PEP,
      ALERT_PREFIX + ALERT_STATUS_KEY, ALERT_STATUS_LEARNING_COMPLETED
  );
  public static final Map<String, Object> ALERT_3_MAP = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP_2,
      DISCRIMINATOR, DISCRIMINATOR_3,
      ALERT_NAME, ALERT_NAME_3,
      ALERT_PREFIX + ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_FP,
      ALERT_PREFIX + ALERT_COUNTRY_KEY, ALERT_COUNTRY_UK,
      ALERT_PREFIX + ALERT_RISK_TYPE_KEY, ALERT_RISK_TYPE_PEP,
      ALERT_PREFIX + ALERT_STATUS_KEY, ALERT_STATUS_COMPLETED
  );
  public static final Map<String, Object> ALERT_4_MAP = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP_3,
      DISCRIMINATOR, DISCRIMINATOR_4,
      ALERT_NAME, ALERT_NAME_4,
      ALERT_PREFIX + ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_MI,
      ALERT_PREFIX + ALERT_COUNTRY_KEY, ALERT_COUNTRY_PL,
      ALERT_PREFIX + ALERT_RISK_TYPE_KEY, ALERT_RISK_TYPE_PEP,
      ALERT_PREFIX + ALERT_STATUS_KEY, ALERT_STATUS_LEARNING_COMPLETED
  );
  public static final Map<String, Object> ALERT_5_MAP = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP_4,
      DISCRIMINATOR, DISCRIMINATOR_5,
      ALERT_NAME, ALERT_NAME_5,
      ALERT_PREFIX + ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_FP,
      ALERT_PREFIX + ALERT_COUNTRY_KEY, ALERT_COUNTRY_PL,
      ALERT_PREFIX + ALERT_RISK_TYPE_KEY, ALERT_RISK_TYPE_PEP,
      ALERT_PREFIX + ALERT_STATUS_KEY, ALERT_STATUS_COMPLETED
  );
  public static final Map<String, Object> ALERT_6_MAP = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP_4,
      DISCRIMINATOR, DISCRIMINATOR_6,
      ALERT_NAME, ALERT_NAME_6,
      ALERT_PREFIX + ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_FP,
      ALERT_PREFIX + ALERT_COUNTRY_KEY, ALERT_COUNTRY_PL,
      ALERT_PREFIX + ALERT_RISK_TYPE_KEY, ALERT_RISK_TYPE_PEP,
      ALERT_PREFIX + ALERT_STATUS_KEY, ALERT_STATUS_ERROR
  );
  public static final Map<String, Object> ALERT_1_MAP_WITHOUT_PREFIX = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP_1,
      DISCRIMINATOR, DISCRIMINATOR_1,
      ALERT_NAME, ALERT_NAME_1,
      ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_FP,
      ALERT_COUNTRY_KEY, ALERT_COUNTRY_UK,
      ALERT_STATUS_KEY, ALERT_STATUS_COMPLETED
  );
  public static final Map<String, Object> ALERT_2_MAP_WITHOUT_PREFIX = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP_1,
      DISCRIMINATOR, DISCRIMINATOR_2,
      ALERT_NAME, ALERT_NAME_2, ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_FP,
      ALERT_COUNTRY_KEY, ALERT_COUNTRY_UK,
      ALERT_RISK_TYPE_KEY, ALERT_RISK_TYPE_PEP,
      ALERT_STATUS_KEY, ALERT_STATUS_LEARNING_COMPLETED
  );
  public static final Map<String, Object> ALERT_3_MAP_WITHOUT_PREFIX = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP_2,
      DISCRIMINATOR, DISCRIMINATOR_3,
      ALERT_NAME, ALERT_NAME_3,
      ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_FP,
      ALERT_COUNTRY_KEY, ALERT_COUNTRY_UK,
      ALERT_RISK_TYPE_KEY, ALERT_RISK_TYPE_PEP,
      ALERT_STATUS_KEY, ALERT_STATUS_COMPLETED
  );
  public static final Map<String, Object> ALERT_4_MAP_WITHOUT_PREFIX = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP_3,
      DISCRIMINATOR, DISCRIMINATOR_4,
      ALERT_NAME, ALERT_NAME_4,
      ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_MI,
      ALERT_COUNTRY_KEY, ALERT_COUNTRY_PL,
      ALERT_RISK_TYPE_KEY, ALERT_RISK_TYPE_PEP,
      ALERT_STATUS_KEY, ALERT_STATUS_LEARNING_COMPLETED
  );
  public static final Map<String, Object> ALERT_5_MAP_WITHOUT_PREFIX = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP_4,
      DISCRIMINATOR, DISCRIMINATOR_5,
      ALERT_NAME, ALERT_NAME_5,
      ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_FP,
      ALERT_COUNTRY_KEY, ALERT_COUNTRY_PL,
      ALERT_RISK_TYPE_KEY, ALERT_RISK_TYPE_PEP,
      ALERT_STATUS_KEY, ALERT_STATUS_COMPLETED
  );
  public static final Map<String, Object> ALERT_6_MAP_WITHOUT_PREFIX = Map.of(
      INDEX_TIMESTAMP, PROCESSING_TIMESTAMP_4,
      DISCRIMINATOR, DISCRIMINATOR_6,
      ALERT_NAME, ALERT_NAME_6,
      ALERT_RECOMMENDATION_KEY, ALERT_RECOMMENDATION_FP,
      ALERT_COUNTRY_KEY, ALERT_COUNTRY_PL,
      ALERT_RISK_TYPE_KEY, ALERT_RISK_TYPE_PEP,
      ALERT_STATUS_KEY, ALERT_STATUS_ERROR
  );
  public static final Timestamp FIRST_DAY_OF_JUNE = Timestamp.newBuilder()
      .setSeconds(1622505601)
      .setNanos(0)
      .build();
  public static final Timestamp FIRST_DAY_OF_APRIL = Timestamp.newBuilder()
      .setSeconds(1617235201)
      .setNanos(0)
      .build();
  static final int REQUESTED_ALERT_COUNT_2 = 2;
  static final int REQUESTED_ALERT_COUNT_4 = 4;
  private static final Timestamp LAST_DAY_OF_JUNE = Timestamp.newBuilder()
      .setSeconds(1625097599)
      .setNanos(0)
      .build();

  private static final RequestedAlertsFilter ALERT_FILTER_BY_COUNTRY_PL =
      RequestedAlertsFilter.newBuilder()
          .setFieldName(ALERT_PREFIX + ALERT_COUNTRY_KEY)
          .setFieldValue(ALERT_COUNTRY_PL)
          .build();

  private static final RequestedAlertsFilter ALERT_FILTER_BY_RISK_TYPE_PEP =
      RequestedAlertsFilter.newBuilder()
          .setFieldName(ALERT_PREFIX + ALERT_RISK_TYPE_KEY)
          .setFieldValue(ALERT_RISK_TYPE_PEP)
          .build();
  static final AlertsSampleRequest ALERTS_SAMPLE_REQUEST_1 = AlertsSampleRequest.newBuilder()
      .setTimeRangeFrom(FIRST_DAY_OF_JUNE)
      .setTimeRangeTo(LAST_DAY_OF_JUNE)
      .addRequestedAlertsFilter(ALERT_FILTER_BY_COUNTRY_PL)
      .addRequestedAlertsFilter(ALERT_FILTER_BY_RISK_TYPE_PEP)
      .setAlertCount(REQUESTED_ALERT_COUNT_2)
      .build();
  static final AlertsSampleRequest ALERTS_SAMPLE_REQUEST_2 = AlertsSampleRequest.newBuilder()
      .setTimeRangeFrom(FIRST_DAY_OF_APRIL)
      .setTimeRangeTo(LAST_DAY_OF_JUNE)
      .addRequestedAlertsFilter(ALERT_FILTER_BY_RISK_TYPE_PEP)
      .setAlertCount(REQUESTED_ALERT_COUNT_4)
      .build();
  private static final RequestedAlertsFilter ALERT_FILTER_BY_NAME1 =
      RequestedAlertsFilter.newBuilder()
          .setFieldName("s8_alert_name")
          .setFieldValue(ALERT_NAME_1)
          .build();
  static final AlertsSampleRequest ALERTS_SAMPLE_REQUEST_3 = AlertsSampleRequest.newBuilder()
      .setTimeRangeFrom(FIRST_DAY_OF_APRIL)
      .setTimeRangeTo(LAST_DAY_OF_JUNE)
      .addRequestedAlertsFilter(ALERT_FILTER_BY_NAME1)
      .setAlertCount(REQUESTED_ALERT_COUNT_4)
      .build();
}
