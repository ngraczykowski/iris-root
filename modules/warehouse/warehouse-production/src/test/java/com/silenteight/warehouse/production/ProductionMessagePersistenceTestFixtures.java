package com.silenteight.warehouse.production;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.Match;
import com.silenteight.data.api.v2.ProductionDataIndexRequest;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.util.Map;

import static java.util.List.of;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ProductionMessagePersistenceTestFixtures {

  static final String ALERT_NAME_1 = "alert name 1";
  static final String ALERT_NAME_2 = "alert name 2";
  static final String ALERT_NAME_3 = "alert name 3";
  static final String ALERT_NAME_4 = "alert name 4";
  static final String MATCH_NAME_1 = "match name 1";
  static final String MATCH_NAME_2 = "match name 2";
  static final String ANALYSIS_NAME = "analysis/120bb00c-8499-4a20-9f1e-e731ea67cbb4";
  static final String MATCH_DISCRIMINATOR_1 = "c09c957d-e4c5-4aab-a39e-fd9dd3c02f85";
  static final String MATCH_DISCRIMINATOR_2 = "81486ef1-7e94-46ac-8bbc-3d69735e6197";
  static final String ALERT_DISCRIMINATOR_1 = "faa4f5c7-07ec-48d8-b4d5-e25b432ea78f";
  static final String ALERT_DISCRIMINATOR_2 = "2a64d3df-d46e-4d73-8fbe-470a2cf63c71";
  static final String ALERT_DISCRIMINATOR_3 = "3e31fc6d-7aba-444e-b731-768b0a9fceb1";
  static final String REQUEST_ID = "7a71928e-2cdb-4fde-a8ed-5cef6d709636";
  static final String RISK_TYPE_KEY = "riskType";
  static final String RISK_TYPE_VALUE = "SAN";
  static final String LOB_COUNTRY_KEY = "lobCountry";
  static final String LOB_COUNTRY_VALUE_1 = "UK";
  static final String LOB_COUNTRY_VALUE_2 = "PL";
  static final String RECOMMENDATION_DATE_KEY = "recommendationDate";
  static final String RECOMMENDATION_DATE_VALUE_1 = "2021-12-03T10:15:30+01:00";
  static final String ALERT_ANALYST_DECISION_KEY = "alertAnalystDecision";
  static final String ALERT_ANALYST_DECISION_VALUE = "FALSE POSITIVE";

  static final Struct PAYLOAD_1 = Struct.newBuilder()
      .putAllFields(Map.of(
          RISK_TYPE_KEY, getValue(RISK_TYPE_VALUE),
          RECOMMENDATION_DATE_KEY, getValue(RECOMMENDATION_DATE_VALUE_1),
          LOB_COUNTRY_KEY, getValue(LOB_COUNTRY_VALUE_1)))
      .build();

  static final Struct PAYLOAD_2 = Struct.newBuilder()
      .putFields(RECOMMENDATION_DATE_KEY, getValue(RECOMMENDATION_DATE_VALUE_1))
      .build();

  static final Struct PAYLOAD_3 = Struct.newBuilder()
      .putAllFields(Map.of(
          ALERT_ANALYST_DECISION_KEY, getValue(ALERT_ANALYST_DECISION_VALUE),
          LOB_COUNTRY_KEY, getValue(LOB_COUNTRY_VALUE_2)))
      .build();

  static final Struct PAYLOAD_4 = Struct.newBuilder()
      .putFields(RECOMMENDATION_DATE_KEY, getNullValue())
      .build();

  static final Match MATCH_1 = Match.newBuilder()
      .setDiscriminator(MATCH_DISCRIMINATOR_1)
      .setPayload(PAYLOAD_1)
      .setName(MATCH_NAME_1)
      .build();

  static final Match MATCH_2 = Match.newBuilder()
      .setDiscriminator(MATCH_DISCRIMINATOR_2)
      .setName(MATCH_NAME_2)
      .build();

  static final Match UPDATED_MATCH_1 = Match.newBuilder()
      .setDiscriminator(MATCH_DISCRIMINATOR_1)
      .setPayload(PAYLOAD_2)
      .setName(MATCH_NAME_2)
      .build();

  static final Alert ALERT_1 = Alert.newBuilder()
      .setDiscriminator(ALERT_DISCRIMINATOR_1)
      .setPayload(PAYLOAD_1)
      .addAllMatches(of(MATCH_1, MATCH_2))
      .setName(ALERT_NAME_1)
      .build();

  static final Alert ALERT_2 = Alert.newBuilder()
      .setDiscriminator(ALERT_DISCRIMINATOR_2)
      .setName(ALERT_NAME_2)
      .setPayload(PAYLOAD_2)
      .build();

  static final Alert ALERT_3 = Alert.newBuilder()
      .setDiscriminator(ALERT_DISCRIMINATOR_3)
      .setName(ALERT_NAME_3)
      .build();

  static final Alert ALERT_4 = Alert.newBuilder()
      .setDiscriminator(ALERT_DISCRIMINATOR_1)
      .setPayload(PAYLOAD_1)
      .addAllMatches(of(MATCH_1))
      .setName(ALERT_NAME_1)
      .build();

  static final Alert UPDATED_ALERT_4 = Alert.newBuilder()
      .setDiscriminator(ALERT_DISCRIMINATOR_1)
      .setPayload(PAYLOAD_3)
      .addAllMatches(of(UPDATED_MATCH_1))
      .setName(ALERT_NAME_4)
      .build();

  static final Alert ALERT_5 = Alert.newBuilder()
      .setDiscriminator(ALERT_DISCRIMINATOR_1)
      .setPayload(PAYLOAD_4)
      .setName(ALERT_NAME_1)
      .build();

  static final ProductionDataIndexRequest PRODUCTION_DATA_INDEX_REQUEST_1 =
      ProductionDataIndexRequest.newBuilder()
          .setAnalysisName(ANALYSIS_NAME)
          .setRequestId(REQUEST_ID)
          .addAllAlerts(of(ALERT_1, ALERT_2))
          .build();

  static final ProductionDataIndexRequest PRODUCTION_DATA_INDEX_REQUEST_2 =
      ProductionDataIndexRequest.newBuilder()
          .setAnalysisName(ANALYSIS_NAME)
          .setRequestId(REQUEST_ID)
          .addAlerts(ALERT_2)
          .build();

  static final ProductionDataIndexRequest PRODUCTION_DATA_INDEX_REQUEST_3 =
      ProductionDataIndexRequest.newBuilder()
          .setAnalysisName(ANALYSIS_NAME)
          .setRequestId(REQUEST_ID)
          .addAlerts(ALERT_3)
          .build();

  static final ProductionDataIndexRequest PRODUCTION_DATA_INDEX_REQUEST_4 =
      ProductionDataIndexRequest.newBuilder()
          .setAnalysisName(ANALYSIS_NAME)
          .setRequestId(REQUEST_ID)
          .addAlerts(ALERT_4)
          .build();

  static final ProductionDataIndexRequest UPDATED_PRODUCTION_DATA_INDEX_REQUEST_4 =
      ProductionDataIndexRequest.newBuilder()
          .setAnalysisName(ANALYSIS_NAME)
          .setRequestId(REQUEST_ID)
          .addAlerts(UPDATED_ALERT_4)
          .build();

  static final ProductionDataIndexRequest PRODUCTION_DATA_INDEX_REQUEST_5 =
      ProductionDataIndexRequest.newBuilder()
          .setAnalysisName(ANALYSIS_NAME)
          .setRequestId(REQUEST_ID)
          .addAlerts(ALERT_5)
          .build();

  private static Value getValue(String value) {
    return Value.newBuilder()
        .setStringValue(value)
        .build();
  }

  private static Value getNullValue() {
    return Value.newBuilder()
        .setNullValueValue(0)
        .build();
  }
}
