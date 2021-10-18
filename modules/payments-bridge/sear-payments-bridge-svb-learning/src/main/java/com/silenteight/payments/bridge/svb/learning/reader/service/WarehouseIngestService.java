package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.common.model.WarehouseAlert;
import com.silenteight.payments.bridge.common.model.WarehouseAnalystSolution;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.JsonFormat.Parser;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class WarehouseIngestService {

  protected static final Parser JSON_TO_STRUCT_PARSER = JsonFormat.parser();
  private final ObjectMapper objectMapper;
  private final CommonChannels commonChannels;

  void ingestReportData(LearningAlert learningAlert) {
    var learningMatch = learningAlert.getMatches().get(0);

    var payloadBuilder = Struct.newBuilder();
    try {
      var alertDataJson = objectMapper.writeValueAsString(
          WarehouseAlert.builder()
              .alertMessageId(learningAlert.getAlertId())
              .fircoSystemId(learningAlert.getSystemId())
              .deliveryStatus("")
              .status("").build());

      // Unknown alert
      JSON_TO_STRUCT_PARSER.merge(alertDataJson, payloadBuilder);
    } catch (InvalidProtocolBufferException | JsonProcessingException e) {
      e.printStackTrace();
    }

    var alertBuilder = Alert.newBuilder()
        .setDiscriminator(learningAlert.getDiscriminator())
        .setPayload(payloadBuilder)
        .build();

    var indexRequest = ProductionDataIndexRequest.newBuilder()
        .addAlerts(alertBuilder)
        .build();

    commonChannels.warehouseRequested().send(
        MessageBuilder.withPayload(indexRequest).build());
  }

  void ingestAnalystSolution(LearningAlert learningAlert) {

    var payloadBuilder = Struct.newBuilder();
    try {
      var analystSolutionJson =
          objectMapper.writeValueAsString(WarehouseAnalystSolution.builder()
              .fircoAnalystDecision(learningAlert.getFircoAnalystDecision())
              .fircoAnalystComment(learningAlert.getFircoAnalystComment())
              .fircoAnalystDecisionTime(learningAlert.getFircoAnalystDecisionTime())
              .build());
      JSON_TO_STRUCT_PARSER.merge(analystSolutionJson, payloadBuilder);

    } catch (InvalidProtocolBufferException | JsonProcessingException e) {
      e.printStackTrace();
    }

    var alertBuilder = Alert.newBuilder()
        .setDiscriminator(learningAlert.getDiscriminator())
        .setPayload(payloadBuilder)
        .build();

    var indexRequest = ProductionDataIndexRequest.newBuilder()
        .addAlerts(alertBuilder)
        .build();
    commonChannels.warehouseRequested().send(
        MessageBuilder.withPayload(indexRequest).build());
  }

}
