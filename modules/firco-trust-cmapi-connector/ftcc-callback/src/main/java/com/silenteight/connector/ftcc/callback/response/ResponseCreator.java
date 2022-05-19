package com.silenteight.connector.ftcc.callback.response;

import lombok.RequiredArgsConstructor;

import com.silenteight.connector.ftcc.callback.newdecision.DecisionMapperUseCase;
import com.silenteight.connector.ftcc.callback.newdecision.DecisionStatusRequest;
import com.silenteight.connector.ftcc.common.dto.input.NextStatusDto;
import com.silenteight.connector.ftcc.common.dto.output.*;
import com.silenteight.connector.ftcc.common.dto.output.AlertDecisionMessageDto.AlertDecisionMessageDtoBuilder;
import com.silenteight.connector.ftcc.common.dto.output.ClientRequestDto.Body;
import com.silenteight.connector.ftcc.request.details.dto.MessageDetailsDto;
import com.silenteight.recommendation.api.library.v1.RecommendationOut;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

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
  @Valid
  private final RecommendationSenderProperties properties;

  public ClientRequestDto build(List<ReceiveDecisionMessageDto> messages) {
    var receiveDecisionDto = ReceiveDecisionDto.builder()
        .messages(messages)
        .authentication(mapToAuthentication(properties.getLogin(), properties.getPassword()))
        .build();

    return ClientRequestDto.builder()
        .body(new Body(receiveDecisionDto))
        .build();
  }

  public ReceiveDecisionMessageDto buildMessageDto(
      MessageDetailsDto messageDetails, RecommendationOut recommendation) {

    return mapToAlertDecision(messageDetails, recommendation);
  }

  private ReceiveDecisionMessageDto mapToAlertDecision(
      MessageDetailsDto messageDetails, RecommendationOut recommendation) {
    //TODO remove defaults when https://silent8.atlassian.net/browse/ALL-737 will be implemented
    String recommendationComment =
        defaultIfBlank(
            recommendation.getRecommendationComment(),
            properties.getDefaultRecommendationComment());
    String recommendationAction =
        defaultIfBlank(recommendation.getRecommendedAction(), "ACTION_INVESTIGATE");

    var decisionBuilder = AlertDecisionMessageDto.builder()
        .unit(messageDetails.getUnit())
        .businessUnit(messageDetails.getBusinessUnit())
        .messageID(messageDetails.getMessageID())
        .systemID(messageDetails.getSystemID())
        .operator(OPERATOR);
    setComment(recommendationComment, decisionBuilder);
    setAttachment(
        messageDetails.getMessageID(), recommendationAction, recommendationComment,
        decisionBuilder);

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
    decisionBuilder.status(destinationStatus.getStatus());

    return create(decisionBuilder.build());
  }

  private static void setAttachment(
      String messageID, String recommendedAction, String recommendationComment,
      AlertDecisionMessageDtoBuilder decision) {
    if (recommendationComment.length() > MAX_COMMENT_LENGTH) {
      decision.attachment(createAttachment(
          messageID,
          recommendedAction,
          recommendationComment));
    }
  }

  private static void setComment(String comment, AlertDecisionMessageDtoBuilder messageDetails) {
    if (comment.length() > MAX_COMMENT_LENGTH) {
      messageDetails.comment(comment.substring(0, getCommentEndIndex()) + COMMENT_CUT_MSG);
    } else {
      messageDetails.comment(comment);
    }
  }

  private static int getCommentEndIndex() {
    return MAX_COMMENT_LENGTH - COMMENT_CUT_MSG.length();
  }

  @NotNull
  private static AttachmentDto createAttachment(
      String messageID, String solution, String comment) {
    var encodedComment =
        Base64.getEncoder().encodeToString(comment.getBytes(StandardCharsets.UTF_8));
    return AttachmentDto.builder()
        .name(getFileneme(messageID))
        .contents(encodedComment)
        .comments(solution)
        .build();
  }

  @NotNull
  private static String getFileneme(String messageID) {
    return messageID + COMMENT_FILE_EXTENSION;
  }


  private static ReceiveDecisionMessageDto create(AlertDecisionMessageDto source) {
    return ReceiveDecisionMessageDto.builder()
        .decisionMessage(source)
        .build();
  }

  private static FircoAuthenticationDto mapToAuthentication(String login, String password) {
    return FircoAuthenticationDto.builder()
        .continuityLogin(login)
        .continuityPassword(password)
        .build();
  }
}
