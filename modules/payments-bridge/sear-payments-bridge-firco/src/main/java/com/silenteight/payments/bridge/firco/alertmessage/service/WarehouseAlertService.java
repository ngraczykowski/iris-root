package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.common.model.WarehouseAlert;
import com.silenteight.payments.bridge.event.AlertAddedToAnalysisEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.JsonFormat.Parser;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Optional;
import java.util.UUID;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_ADDED_TO_ANALYSIS;

@MessageEndpoint
@RequiredArgsConstructor
@Slf4j
class WarehouseAlertService {

  protected static final Parser JSON_TO_STRUCT_PARSER = JsonFormat.parser();

  private final ObjectMapper objectMapper;
  private final AlertMessageStatusService alertMessageStatusService;
  private final CommonChannels commonChannels;

  @ServiceActivator(inputChannel = ALERT_ADDED_TO_ANALYSIS)
  void accept(AlertAddedToAnalysisEvent event) {
    var alertData = event.getData(AlertData.class);

    buildWarehouseAlert(event, alertData).ifPresent(payload -> {
      var alertBuilder = Alert.newBuilder()
          .setAccessPermissionTag("US")
          .setDiscriminator(alertData.getDiscriminator())
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

  private Optional<Struct> buildWarehouseAlert(
      AlertAddedToAnalysisEvent event, AlertData alertData) {
    var status = alertMessageStatusService.findByAlertId(event.getAlertId());
    var payloadBuilder = Struct.newBuilder();
    try {
      var alertDataJson = objectMapper.writeValueAsString(
          WarehouseAlert.builder()
              .alertMessageId(event.getAlertId().toString())
              .fircoSystemId(alertData.getSystemId())
              .accessPermissionTag("US")
              .deliveryStatus("") // TODO:
              .status(status.getStatus().name()).build());
      JSON_TO_STRUCT_PARSER.merge(alertDataJson, payloadBuilder);
      return Optional.of(payloadBuilder.build());
    } catch (InvalidProtocolBufferException | JsonProcessingException e) {
      log.error("Could not convert to WarehouseAlert payload", e);
      return Optional.empty();
    }

  }
}
