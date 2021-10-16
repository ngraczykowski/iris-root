package com.silenteight.payments.bridge.firco.callback.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.dto.input.NextStatusDto;
import com.silenteight.payments.bridge.common.dto.output.*;
import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.decision.MapStatusRequest;
import com.silenteight.payments.bridge.firco.decision.MapStatusUseCase;
import com.silenteight.payments.bridge.svb.oldetl.util.StringUtil;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
class ClientRequestDtoMapper {

  private static final String ATTACHMENT_COMMENT_NAME = "comment.txt";
  private static final String COMMENT = "S8 Recommendation: Manual Investigation";
  private static final String COMMENT_CUT_MSG = " === See attachment for full comments ===";
  private static final int MAX_COMMENT_LENGTH = 1024;
  private final MapStatusUseCase mapStatusUseCase;

  ClientRequestDto mapToAlertDecision(
      AlertData alert,
      AlertMessageDto alertDto, AlertMessageStatus status) {
    var decision = new AlertDecisionMessageDto();
    decision.setUnit(alertDto.getUnit());
    decision.setBusinessUnit(alertDto.getBusinessUnit());
    decision.setMessageID(alertDto.getMessageID());
    decision.setSystemID(alertDto.getSystemID());
    decision.setOperator(alert.getUserLogin());
    decision.setActions(List.of());
    decision.setAttachment(createAttachment(COMMENT));
    decision.setUserLogin(alert.getUserLogin());
    decision.setUserPassword(alert.getUserPassword());
    setComment(COMMENT, decision);

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

  private void setComment(String comment, AlertDecisionMessageDto messageDetails) {
    if (comment.length() > MAX_COMMENT_LENGTH) {
      messageDetails.setComment(comment.substring(0, getCommentEndIndex()) + COMMENT_CUT_MSG);
    } else {
      messageDetails.setComment(comment);
    }
  }

  private int getCommentEndIndex() {
    return MAX_COMMENT_LENGTH - COMMENT_CUT_MSG.length();
  }

  @NotNull
  private static AttachmentDto createAttachment(String comment) {
    String encoded = StringUtil.lfToCrLf(
        Base64.getEncoder().encodeToString(
            comment.getBytes(StandardCharsets.UTF_8)));
    return new AttachmentDto(ATTACHMENT_COMMENT_NAME, encoded);
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

  static FircoAuthenticationDto mapToAuthentication(AlertDecisionMessageDto source) {
    FircoAuthenticationDto authentication = new FircoAuthenticationDto();
    authentication.setContinuityLogin(source.getUserLogin());
    authentication.setContinuityPassword(source.getUserPassword());
    authentication.setContinuityBusinessUnit(source.getBusinessUnit());
    return authentication;
  }

}
