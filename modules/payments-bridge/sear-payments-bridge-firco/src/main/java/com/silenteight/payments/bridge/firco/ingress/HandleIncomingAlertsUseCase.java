package com.silenteight.payments.bridge.firco.ingress;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.payments.bridge.common.protobuf.TimestampConverter;
import com.silenteight.payments.bridge.firco.dto.input.AlertDataCenterDto;
import com.silenteight.proto.payments.bridge.internal.v1.AcceptAlertCommand;
import com.silenteight.proto.payments.bridge.internal.v1.AlertMessage;
import com.silenteight.proto.payments.bridge.internal.v1.RejectAlertCommand;
import com.silenteight.proto.payments.bridge.internal.v1.RejectAlertCommand.Reason;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
class HandleIncomingAlertsUseCase {

  private final AcceptAlertGateway acceptAlertGateway;
  private final DelayedRejectAlertGateway delayedRejectAlertGateway;
  private final AlertMessageFactory alertMessageFactory;

  @Setter
  private Clock clock = Clock.systemUTC();

  void handleIncomingAlerts(List<AlertDataCenterDto> alertDataCenterDtos) {
    var receiveTime = TimestampConverter.fromInstant(Instant.now(clock));
    alertDataCenterDtos.stream()
        .map(dto -> alertMessageFactory.create(
            dto.getAlertMessageDto(), receiveTime, makeEndpoint(dto.getDataCenter())))
        .forEach(this::doHandleIncomingAlert);
  }

  private static String makeEndpoint(String dataCenter) {
    return "recommendation-endpoints/" + dataCenter;
  }

  private void doHandleIncomingAlert(AlertMessage alertMessage) {
    acceptAlert(alertMessage);
    delayedRejectAlert(alertMessage);
  }

  private void acceptAlert(AlertMessage alertMessage) {
    var acceptCommand = AcceptAlertCommand.newBuilder()
        .setAlertMessage(alertMessage)
        .build();

    acceptAlertGateway.sendMessage(acceptCommand);
  }

  private void delayedRejectAlert(AlertMessage alertMessage) {
    var rejectCommand = RejectAlertCommand.newBuilder()
        .setAlertMessage(alertMessage)
        .setReason(Reason.OUTDATED)
        .build();

    delayedRejectAlertGateway.sendMessage(rejectCommand);
  }
}
