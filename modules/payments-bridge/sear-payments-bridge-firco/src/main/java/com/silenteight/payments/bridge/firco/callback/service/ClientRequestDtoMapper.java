package com.silenteight.payments.bridge.firco.callback.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.dto.common.StatusInfoDto;
import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.dto.input.NextStatusDto;
import com.silenteight.payments.bridge.common.dto.output.*;
import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.decision.MapStatusRequest;
import com.silenteight.payments.bridge.firco.decision.MapStatusUseCase;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
class ClientRequestDtoMapper {

  private static final String COMMENT = "MANUAL_INVESTIGATION";
  private static final String OPERATOR = "Silent Eight";

  private final MapStatusUseCase mapStatusUseCase;

  ClientRequestDto mapToAlertDecision(AlertData alert,
      AlertMessageDto alertDto, AlertMessageStatus status) {
    var decision = new AlertDecisionMessageDto();
    decision.setUnit(alertDto.getUnit());
    decision.setBusinessUnit(alertDto.getBusinessUnit());
    decision.setMessageId(alertDto.getMessageID());
    decision.setSystemId(alertDto.getSystemID());
    decision.setOperator(OPERATOR);
    decision.setComment(COMMENT);
    decision.setActions(List.of());

    var destinationStatus = mapStatusUseCase.mapStatus(
        MapStatusRequest.builder()
            .dataCenter(alert.getDataCenter())
            .unit(alertDto.getUnit())
            .nextStatuses(alertDto.getNextStatuses()
                .stream()
                .map(NextStatusDto::getStatus)
                .collect(Collectors.toList()))
            .currentStatusName(alertDto.getCurrentStatus().getName())
            .recommendedAction(COMMENT)
            .build());
    decision.setStatus(destinationStatus.getStatus());
    return create(decision);
  }

  private StatusInfoDto toStatusInfoDto(StatusInfoDto info) {
    return new StatusInfoDto(info.getId(), info.getName(),
        info.getRoutingCode(), info.getChecksum());
  }

  private ClientRequestDto create(AlertDecisionMessageDto source) {
    var receiveDecisionMessageDto = new ReceiveDecisionMessageDto();
    receiveDecisionMessageDto.setDecisionMessage(source);

    var receiveDecisionDto = new ReceiveDecisionDto();
    receiveDecisionDto.setMessages(List.of(receiveDecisionMessageDto));
    receiveDecisionDto.setAuthentication(mapToAuthentication(source));

    var clientRequestDto = new ClientRequestDto();
    clientRequestDto.setReceiveDecisionDto(receiveDecisionDto);
    return clientRequestDto;
  }

  private FircoAuthenticationDto mapToAuthentication(AlertDecisionMessageDto source) {
    FircoAuthenticationDto authentication = new FircoAuthenticationDto();
    authentication.setContinuityLogin("Login");
    authentication.setContinuityPassword("Password");
    authentication.setContinuityBusinessUnit(source.getBusinessUnit());
    return authentication;
  }

}
