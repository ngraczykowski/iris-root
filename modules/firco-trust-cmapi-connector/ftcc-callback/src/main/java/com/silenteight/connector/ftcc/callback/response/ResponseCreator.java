package com.silenteight.connector.ftcc.callback.response;

import lombok.RequiredArgsConstructor;

import com.silenteight.connector.ftcc.callback.newdecision.DecisionMapperUseCase;
import com.silenteight.connector.ftcc.callback.newdecision.DecisionStatusRequest;
import com.silenteight.connector.ftcc.common.dto.input.NextStatusDto;
import com.silenteight.connector.ftcc.common.dto.output.*;
import com.silenteight.connector.ftcc.request.details.dto.MessageDetailsDto;
import com.silenteight.recommendation.api.library.v1.RecommendationOut;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableConfigurationProperties(RecommendationSenderProperties.class)
@RequiredArgsConstructor
class ResponseCreator {

  private static final String ATTACHMENT_COMMENT_NAME = "attachment.txt";
  private static final String COMMENT_CUT_MSG = "\n\n=== See attachment for full comments ===";
  private static final int MAX_COMMENT_LENGTH = 1024;
  public static final String DATA_CENTER = "";
  public static final String OPERATOR = "S8 SEAR";

  private final DecisionMapperUseCase decisionMapperUseCase;
  private final RecommendationSenderProperties properties;

  public ClientRequestDto build(List<ReceiveDecisionMessageDto> messages) {
    var receiveDecisionDto = new ReceiveDecisionDto();
    receiveDecisionDto.setMessages(messages);
    receiveDecisionDto.setAuthentication(
        mapToAuthentication(properties.getLogin(), properties.getPassword()));

    var clientRequestDto = new ClientRequestDto();
    clientRequestDto.setReceiveDecisionDto(receiveDecisionDto);
    return clientRequestDto;
  }

  public ReceiveDecisionMessageDto buildMessageDto(
      MessageDetailsDto messageDetails, RecommendationOut recommendation) {

    return mapToAlertDecision(messageDetails, recommendation);
  }

  private ReceiveDecisionMessageDto mapToAlertDecision(
      MessageDetailsDto messageDetails, RecommendationOut recommendation) {

    var decision = new AlertDecisionMessageDto();
    decision.setUnit(messageDetails.getUnit());
    decision.setBusinessUnit(messageDetails.getBusinessUnit());
    decision.setMessageID(messageDetails.getMessageID());
    decision.setSystemID(messageDetails.getSystemID());
    decision.setOperator(OPERATOR);
    /*TODO: verify attachment*/
    decision.setAttachment(createAttachment(
        recommendation.getRecommendedAction(),
        recommendation.getRecommendationComment()));
    setComment(recommendation.getRecommendationComment(), decision);

    var destinationStatus = decisionMapperUseCase.mapStatus(
        DecisionStatusRequest.builder()
            .dataCenter(DATA_CENTER)
            .unit(messageDetails.getUnit())
            .nextStatuses(messageDetails.nextStatusesDto()
                .stream()
                .map(NextStatusDto::getStatus)
                .collect(Collectors.toList()))
            .currentStatusName(messageDetails.getCurrentStatus().getName())
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
  private static AttachmentDto createAttachment(String solution, String comment) {
    var encodedComment =
        Base64.getEncoder().encodeToString(comment.getBytes(StandardCharsets.UTF_8));
    return new AttachmentDto(ATTACHMENT_COMMENT_NAME, encodedComment, solution);
  }


  private static ReceiveDecisionMessageDto create(AlertDecisionMessageDto source) {
    var receiveDecisionMessageDto = new ReceiveDecisionMessageDto();
    receiveDecisionMessageDto.setDecisionMessage(source);
    return receiveDecisionMessageDto;
  }

  private static FircoAuthenticationDto mapToAuthentication(String login, String password) {
    FircoAuthenticationDto authentication = new FircoAuthenticationDto();
    authentication.setContinuityLogin(login);
    authentication.setContinuityPassword(password);
    return authentication;
  }
}
