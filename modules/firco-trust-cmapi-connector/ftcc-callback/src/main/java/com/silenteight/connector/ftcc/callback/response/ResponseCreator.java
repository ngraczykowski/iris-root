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

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

@Component
@EnableConfigurationProperties(RecommendationSenderProperties.class)
@RequiredArgsConstructor
class ResponseCreator {

  private static final String COMMENT_CUT_MSG =
      "\n\nPlease find the attachment for review comments";
  private static final int MAX_COMMENT_LENGTH = 1024;
  public static final String DATA_CENTER = "";
  public static final String OPERATOR = "S8 SEAR";
  private static final String COMMENT_FILE_EXTENSION = ".txt";

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
    //TODO remove defaults when https://silent8.atlassian.net/browse/ALL-737 will be implemented
    String recommendationComment =
        defaultIfBlank(recommendation.getRecommendationComment(), "Manual Investigation");
    String recommendationAction =
        defaultIfBlank(recommendation.getRecommendedAction(), "ACTION_INVESTIGATE");

    var decision = new AlertDecisionMessageDto();
    decision.setUnit(messageDetails.getUnit());
    decision.setBusinessUnit(messageDetails.getBusinessUnit());
    decision.setMessageID(messageDetails.getMessageID());
    decision.setSystemID(messageDetails.getSystemID());
    decision.setOperator(OPERATOR);
    setComment(recommendationComment, decision);
    setAttachment(
        messageDetails.getMessageID(), recommendationAction, recommendationComment,
        decision);

    var destinationStatus = decisionMapperUseCase.mapStatus(
        DecisionStatusRequest.builder()
            .dataCenter(DATA_CENTER)
            .unit(messageDetails.getUnit())
            .nextStatuses(messageDetails.nextStatusesDto()
                .stream()
                .map(NextStatusDto::getStatus)
                .collect(Collectors.toList()))
            .currentStatusName(messageDetails.getCurrentStatus().getName())
            .recommendedAction(recommendationAction)
            .build());
    decision.setStatus(destinationStatus.getStatus());

    return create(decision);
  }

  private void setAttachment(
      String messageID, String recommendedAction, String recommendationComment,
      AlertDecisionMessageDto decision) {
    if (recommendationComment.length() > MAX_COMMENT_LENGTH) {
      decision.setAttachment(createAttachment(
          messageID,
          recommendedAction,
          recommendationComment));
    }
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
  private static AttachmentDto createAttachment(
      String messageID, String solution, String comment) {
    var encodedComment =
        Base64.getEncoder().encodeToString(comment.getBytes(StandardCharsets.UTF_8));
    return new AttachmentDto(getFileneme(messageID), encodedComment, solution);
  }

  @NotNull
  private static String getFileneme(String messageID) {
    return messageID + COMMENT_FILE_EXTENSION;
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
