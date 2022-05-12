package com.silenteight.warehouse.report.statistics.simulation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.warehouse.production.persistence.mapping.alert.AlertDefinition;
import com.silenteight.warehouse.report.statistics.simulation.dto.SimulationStatisticsDto;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.report.statistics.simulation.dto.SimulationStatisticsDto.EffectivenessDto;
import static com.silenteight.warehouse.report.statistics.simulation.dto.SimulationStatisticsDto.EfficiencyDto;
import static com.silenteight.warehouse.report.statistics.simulation.dto.SimulationStatisticsDto.builder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimulationStatisticsTestFixtures {

  static final String REQUEST_ID_1 = "c06d019f-6eb3-4bcb-9268-02d222b1b8a8";
  static final String ANALYSIS_ID_1 = "03acd31b-ff62-466c-a524-d81bfb596aa5";
  static final String ANALYSIS_NAME_1 = "analysis/" + ANALYSIS_ID_1;
  static final String ALERT_DISCRIMINATOR_1 = "5c4b0db0-94cf-4f8f-9d89-3d063590edf6";
  static final String ALERT_DISCRIMINATOR_2 = "6443fe45-8522-4817-9b11-985701f669b1";
  static final String ALERT_DISCRIMINATOR_3 = "f408497d-c3ed-4a4c-aaeb-c642f506b222";
  static final String ALERT_NAME_1 = "alerts/2469af08-2825-4cdd-9650-e5b1a278bce8";
  static final String ALERT_NAME_2 = "alerts/8a85d0b7-f3f5-4e78-850d-3b3fcddb7a6a";
  static final String ALERT_NAME_3 = "alerts/f155eb98-1379-4e42-89a1-c98f0b2eb0ca";
  static final String ALERT_PAYLOAD_1 = "{\"DN_CASE.currentState\": \"Level 1 Review\", "
      + "\"recommendation_recommended_action\": \"ACTION_FALSE_POSITIVE\"}";

  static final String ALERT_PAYLOAD_2 = "{\"DN_CASE.currentState\": \"Level 2 Review\", "
      + "\"recommendation_recommended_action\": \"ACTION_FALSE_POSITIVE\"}";

  static final String ALERT_PAYLOAD_3 = "{\"DN_CASE.currentState\": \"Awaiting AAA Adjudication\", "
      + "\"recommendation_recommended_action\": \"ACTION_INVESTIGATE\"}";

  static final AlertDefinition ALERT_1 = AlertDefinition.builder()
      .discriminator(ALERT_DISCRIMINATOR_1)
      .name(ALERT_NAME_1)
      .payload(ALERT_PAYLOAD_1)
      .recommendationDate(OffsetDateTime.now())
      .labels(Map.of())
      .build();

  static final AlertDefinition ALERT_2 = AlertDefinition.builder()
      .discriminator(ALERT_DISCRIMINATOR_2)
      .name(ALERT_NAME_2)
      .payload(ALERT_PAYLOAD_2)
      .recommendationDate(OffsetDateTime.now())
      .labels(Map.of())
      .build();

  static final AlertDefinition ALERT_3 = AlertDefinition.builder()
      .discriminator(ALERT_DISCRIMINATOR_3)
      .name(ALERT_NAME_3)
      .payload(ALERT_PAYLOAD_3)
      .recommendationDate(OffsetDateTime.now())
      .labels(Map.of())
      .build();

  static final com.silenteight.data.api.v1.Alert ALERT_SIM_1 =
      com.silenteight.data.api.v1.Alert
          .newBuilder()
          .setDiscriminator(ALERT_DISCRIMINATOR_1)
          .setName(ALERT_NAME_1)
          .setPayload(
              convertToPayload("recommendation_recommended_action", "ACTION_FALSE_POSITIVE"))
          .build();

  static final com.silenteight.data.api.v1.Alert ALERT_SIM_2 =
      com.silenteight.data.api.v1.Alert
          .newBuilder()
          .setDiscriminator(ALERT_DISCRIMINATOR_2)
          .setName(ALERT_NAME_2)
          .setPayload(
              convertToPayload("recommendation_recommended_action", "ACTION_FALSE_POSITIVE"))
          .build();

  static final com.silenteight.data.api.v1.Alert ALERT_SIM_3 =
      com.silenteight.data.api.v1.Alert
          .newBuilder()
          .setDiscriminator(ALERT_DISCRIMINATOR_3)
          .setName(ALERT_NAME_3)
          .setPayload(convertToPayload(
              "recommendation_recommended_action", "ACTION_INVESTIGATE"))
          .build();

  static final SimulationDataIndexRequest SIM_INDEX_REQUEST_1 =
      SimulationDataIndexRequest.newBuilder()
          .setRequestId(REQUEST_ID_1)
          .setAnalysisName(ANALYSIS_NAME_1)
          .addAllAlerts(List.of(ALERT_SIM_1, ALERT_SIM_2, ALERT_SIM_3))
          .build();

  static final EffectivenessDto EFFECTIVENESS_DTO = EffectivenessDto
      .builder()
      .analystSolvedAsFalsePositive(10L)
      .aiSolvedAsFalsePositive(5L)
      .build();

  static final EfficiencyDto EFFICIENCY_DTO = EfficiencyDto
      .builder()
      .solvedAlerts(20L)
      .allAlerts(25L)
      .build();

  public static final SimulationStatisticsDto STATISTICS_DTO = builder()
      .effectiveness(EFFECTIVENESS_DTO)
      .efficiency(EFFICIENCY_DTO)
      .build();

  private static Struct convertToPayload(String key, String value) {
    return Struct.newBuilder()
        .putFields(key, toValue(value))
        .build();
  }

  private static Value toValue(String value) {
    return Value.newBuilder().setStringValue(value).build();
  }
}
