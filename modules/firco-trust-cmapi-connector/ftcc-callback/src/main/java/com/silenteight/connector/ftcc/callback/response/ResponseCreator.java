package com.silenteight.connector.ftcc.callback.response;

import lombok.RequiredArgsConstructor;

import com.silenteight.connector.ftcc.callback.decision.MapStatusRequest;
import com.silenteight.connector.ftcc.callback.decision.MapStatusUseCase;
import com.silenteight.connector.ftcc.callback.response.domain.MessageEntity;
import com.silenteight.connector.ftcc.common.dto.input.NextStatusDto;
import com.silenteight.connector.ftcc.common.dto.output.*;
import com.silenteight.recommendation.api.library.v1.RecommendationOut;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class ResponseCreator {

  private static final String ATTACHMENT_COMMENT_NAME = "attachment.txt";
  private static final String COMMENT_CUT_MSG = "\n\n=== See attachment for full comments ===";
  private static final int MAX_COMMENT_LENGTH = 1024;
  public static final String DATA_CENTER = "";

  private final MapStatusUseCase mapStatusUseCase;

  public ClientRequestDto create(MessageEntity messageEntity, RecommendationOut recommendation) {
    return mapToAlertDecision(messageEntity, recommendation);
  }

  private ClientRequestDto mapToAlertDecision(
      MessageEntity messageEntity, RecommendationOut recommendation) {

    var decision = new AlertDecisionMessageDto();
    decision.setUnit(messageEntity.getUnit());
    decision.setBusinessUnit(messageEntity.getBusinessUnit());
    decision.setMessageID(messageEntity.getMessageID());
    decision.setSystemID(messageEntity.getSystemID());
    decision.setOperator(messageEntity.getLastOperator());
    decision.setActions(List.of());
    /*TODO: verify attachment*/
    decision.setAttachment(createAttachment(
        recommendation.getRecommendedAction(),
        recommendation.getRecommendationComment()));
    /*decision.setUserLogin(alert.getUserLogin());*/
    // decision.setUserPassword(alert.getUserPassword());
    setComment(recommendation.getRecommendationComment(), decision);

    var destinationStatus = mapStatusUseCase.mapStatus(
        MapStatusRequest.builder()
            .dataCenter(DATA_CENTER)
            .unit(messageEntity.getUnit())
            .nextStatuses(messageEntity.nextStatusesDto()
                .stream()
                .map(NextStatusDto::getStatus)
                .collect(Collectors.toList()))
            .currentStatusName(messageEntity.getCurrentStatus().getName())
            .recommendedAction(recommendation.getRecommendedAction())
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
  private static AttachmentDto createAttachment(String contents, String comment) {
    var encodedComment =
        Base64.getEncoder().encodeToString(comment.getBytes(StandardCharsets.UTF_8));
    return new AttachmentDto(ATTACHMENT_COMMENT_NAME, contents, encodedComment);
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
