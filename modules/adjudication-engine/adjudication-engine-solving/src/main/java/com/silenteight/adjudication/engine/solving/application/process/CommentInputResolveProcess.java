package com.silenteight.adjudication.engine.solving.application.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.commentinput.CommentInputClient;
import com.silenteight.adjudication.engine.comments.commentinput.CommentInputResponse;
import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.solving.application.process.port.CommentInputResolveProcessPort;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInput;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInputClientRepository;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInputStoreService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
class CommentInputResolveProcess implements CommentInputResolveProcessPort {

  private final CommentInputClient commentInputClient;
  private final ProtoMessageToObjectNodeConverter converter;
  private final CommentInputClientRepository commentInputClientRepository;
  private final CommentInputStoreService commentInputStoreService;

  public void retrieveCommentInput(String alertID) {
    log.debug("Alert id: {} has been determined", alertID);
    final List<CommentInputResponse> commentInputsResponse =
        this.commentInputClient.getCommentInputsResponse(List.of(alertID));

    if (log.isDebugEnabled()) {
      log.debug("Retrieved comments inputs: {}", commentInputsResponse);
    }
    commentInputsResponse.forEach(this::prepareUpdateCommand);
  }

  private void prepareUpdateCommand(CommentInputResponse commentInputResponse) {
    log.debug(
        "Process match comment input response for alert: {} ", commentInputResponse.getAlert());
    long alertId = ResourceName.create(commentInputResponse.getAlert()).alertId();

    final String alertCommentInputsMap =
        converter.convertToJsonString(commentInputResponse.getAlertCommentInput()).orElseThrow();

    this.commentInputClientRepository.store(alertId, alertCommentInputsMap);
    this.commentInputStoreService.store(new CommentInput(alertId, alertCommentInputsMap));
  }
}
