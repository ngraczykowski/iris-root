package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
class WarehouseIngestService {

  protected static final Parser JSON_TO_STRUCT_PARSER = JsonFormat.parser();
  private final ObjectMapper objectMapper;
  private final CommonChannels commonChannels;

  void ingestReportData(LearningAlert learningAlert) {
    buildWarehouseAlert(learningAlert).ifPresent(payload -> {
      var alertBuilder = Alert.newBuilder()
          .setAccessPermissionTag("US")
          .setDiscriminator(learningAlert.getDiscriminator())
          .setPayload(payload)
          .build();

      var indexRequest = ProductionDataIndexRequest.newBuilder()
          .setRequestId(UUID.randomUUID().toString())
          .addAlerts(alertBuilder)
          .build();

      commonChannels.warehouseRequested().send(
          MessageBuilder.withPayload(indexRequest).build());
    });
  }

  private Optional<Struct> buildWarehouseAlert(LearningAlert learningAlert) {
    var payloadBuilder = Struct.newBuilder();
    try {
      var alertDataJson = objectMapper.writeValueAsString(
          WarehouseAlert.builder()
              .alertMessageId(learningAlert.getAlertId())
              .fircoSystemId(learningAlert.getSystemId())
              .deliveryStatus("")
              .status("").build());
      JSON_TO_STRUCT_PARSER.merge(alertDataJson, payloadBuilder);
      return Optional.of(payloadBuilder.build());
    } catch (InvalidProtocolBufferException | JsonProcessingException e) {
      log.error("Could not convert to WarehouseAlert payload", e);
      return Optional.empty();
    }
  }

  void ingestAnalystSolution(LearningAlert learningAlert) {
    buildWarehouseAnalystSolution(learningAlert).ifPresent(payload -> {
      var alertBuilder = Alert.newBuilder()
          .setAccessPermissionTag("US")
          .setDiscriminator(learningAlert.getDiscriminator())
          .setPayload(payload)
          .build();

      var indexRequest = ProductionDataIndexRequest.newBuilder()
          .setRequestId(UUID.randomUUID().toString())
          .addAlerts(alertBuilder)
          .build();
      commonChannels.warehouseRequested().send(
          MessageBuilder.withPayload(indexRequest).build());
    });
  }

  private Optional<Struct> buildWarehouseAnalystSolution(LearningAlert learningAlert) {
    var payloadBuilder = Struct.newBuilder();
    try {
      var analystSolutionJson =
          objectMapper.writeValueAsString(
              WarehouseAnalystSolution.builder()
                .fircoAnalystDecision(learningAlert.getFircoAnalystDecision())
                .fircoAnalystComment(learningAlert.getFircoAnalystComment())
                .fircoAnalystDecisionTime(learningAlert.getFircoAnalystDecisionTime())
                .build());
      JSON_TO_STRUCT_PARSER.merge(analystSolutionJson, payloadBuilder);
      return Optional.of(payloadBuilder.build());
    } catch (InvalidProtocolBufferException | JsonProcessingException e) {
      log.error("Could not convert to WarehouseAnalystSolution payload", e);
      return Optional.empty();
    }
  }

}
