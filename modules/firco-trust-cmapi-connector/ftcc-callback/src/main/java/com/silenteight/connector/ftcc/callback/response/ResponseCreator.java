package com.silenteight.connector.ftcc.callback.response;

import com.silenteight.connector.ftcc.common.dto.input.AlertMessageDto;
import com.silenteight.connector.ftcc.common.dto.output.*;
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;
import com.silenteight.recommendation.api.library.v1.RecommendationOut;
import com.silenteight.recommendation.api.library.v1.RecommendationsOut;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Component
class ResponseCreator {

  private static final String ATTACHMENT_COMMENT_NAME = "attachment.txt";
  private static final String COMMENT_CUT_MSG = "\n\n=== See attachment for full comments ===";
  private static final int MAX_COMMENT_LENGTH = 1024;

  public ClientRequestDto create(
      MessageBatchCompleted messageBatchCompleted,
      RecommendationsOut recommendation) {
    var source = new AlertDecisionMessageDto();
    return mapToAlertDecision(null, null);
  }

  private ClientRequestDto mapToAlertDecision(
      AlertMessageDto alertDto, RecommendationOut recommendation) {

    var decision = new AlertDecisionMessageDto();
    decision.setUnit(alertDto.getUnit());
    decision.setBusinessUnit(alertDto.getBusinessUnit());
    decision.setMessageID(alertDto.getMessageID());
    decision.setSystemID(alertDto.getSystemID());
    /*
      decision.setOperator(alert.getUserLogin());
    */
    decision.setActions(List.of());
    /*TODO: verify attachment*/
    decision.setAttachment(createAttachment(
        recommendation.getRecommendedAction(),
        recommendation.getRecommendationComment()));
    /*decision.setUserLogin(alert.getUserLogin());*/
    // decision.setUserPassword(alert.getUserPassword());
    setComment(recommendation.getRecommendationComment(), decision);

    /*
    var destinationStatus = mapStatusUseCase.mapStatus(
        MapStatusRequest.builder()
            .dataCenter(alert.getDataCenter())
            .unit(alertDto.getUnit())
            .nextStatuses(alertDto.getNextStatuses()
                .stream()
                .map(NextStatusDto::getStatus)
                .collect(Collectors.toList()))
            .currentStatusName(alertDto.getCurrentStatus().getName())
            .recommendedAction(recommendation.getRecommendedAction())
            .build());
    decision.setStatus(destinationStatus.getStatus());*/

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
