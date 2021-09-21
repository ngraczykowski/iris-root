package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.core.decision.MapStatusRequest;
import com.silenteight.payments.bridge.firco.core.decision.MapStatusUseCase;
import com.silenteight.payments.bridge.firco.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.firco.dto.input.NextStatusDto;
import com.silenteight.payments.bridge.firco.dto.output.AlertDecisionMessageDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
class AlertDecisionMapper {

  private static final String COMMENT = "MANUAL_INVESTIGATION";
  private static final String OPERATOR = "Silent Eight";

  private final MapStatusUseCase mapStatusUseCase;
  private final ObjectMapper objectMapper;

  AlertDecisionMessageDto mapToAlertDecision(AlertMessageEntity entity,
      AlertMessagePayload payload, AlertMessageStatus status) {
    var decision = new AlertDecisionMessageDto();
    decision.setUnit(entity.getUnit());
    decision.setBusinessUnit(entity.getBusinessUnit());
    decision.setMessageId(entity.getMessageId());
    decision.setSystemId(entity.getSystemId());
    decision.setOperator(OPERATOR);
    decision.setComment(COMMENT);
    decision.setActions(List.of());

    var alertMessageDto = objectMapper
        .convertValue(payload.getOriginalMessage(), AlertMessageDto.class);

    var destinationStatus = mapStatusUseCase.mapStatus(
        MapStatusRequest.builder()
          .dataCenter(entity.getDataCenter())
          .unit(entity.getUnit())
          .nextStatuses(alertMessageDto.getNextStatuses()
              .stream()
              .map(NextStatusDto::getStatus)
              .collect(Collectors.toList()))
          .currentStatusName(alertMessageDto.getCurrentStatus().getName())
          .recommendedAction(COMMENT)
          .build());
    decision.setStatus(destinationStatus.getStatus());
    return decision;
  }

}
