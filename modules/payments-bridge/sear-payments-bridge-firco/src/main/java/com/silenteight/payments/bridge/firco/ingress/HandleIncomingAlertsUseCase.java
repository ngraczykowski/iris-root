package com.silenteight.payments.bridge.firco.ingress;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.payments.bridge.common.protobuf.TimestampConverter;
import com.silenteight.payments.bridge.firco.dto.input.AlertDataCenterDto;
import com.silenteight.sear.payments.bridge.internal.v1.AlertMessage;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
class HandleIncomingAlertsUseCase {

  private final AlertMessageGateway alertMessageGateway;

  @Setter
  private Clock clock = Clock.systemUTC();

  void handleIncomingAlerts(List<AlertDataCenterDto> alertDataCenterDtos) {
    var receiveTime = TimestampConverter.fromInstant(Instant.now(clock));

    alertDataCenterDtos.stream()
        .map(dto -> createMessage(dto, receiveTime))
        .forEach(alertMessageGateway::sendMessage);
  }

  private static AlertMessage createMessage(AlertDataCenterDto dto, Timestamp receiveTime) {
    return AlertMessage.newBuilder()
        .setDecisionEndpoint("decision-endpoints/" + dto.getDataCenter())
        // TODO(ahaczewski): Set the receive time.
        // TODO(ahaczewski): Map the original alert to Struct.
        .build();
  }
}
