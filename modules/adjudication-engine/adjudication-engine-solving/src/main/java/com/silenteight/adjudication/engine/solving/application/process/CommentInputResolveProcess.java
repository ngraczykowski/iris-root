package com.silenteight.adjudication.engine.solving.application.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.commentinput.CommentInputClient;
import com.silenteight.adjudication.engine.comments.commentinput.CommentInputResponse;
import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInputClientRepository;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
class CommentInputResolveProcess {

  private final CommentInputClient commentInputClient;

  private final ProtoMessageToObjectNodeConverter converter;
  private final CommentInputClientRepository commentInputClientRepository;
  private final Queue<String> alertCommentsInputQueue;

  CommentInputResolveProcess(
      final CommentInputClient commentInputClient,
      final ProtoMessageToObjectNodeConverter converter,
      final CommentInputClientRepository commentInputClientRepository,
      final ScheduledExecutorService scheduledExecutorService,
      final Queue<String> alertCommentsInputQueue) {
    this.commentInputClient = commentInputClient;
    this.converter = converter;
    this.commentInputClientRepository = commentInputClientRepository;
    this.alertCommentsInputQueue = alertCommentsInputQueue;
    scheduledExecutorService.scheduleAtFixedRate(this::process, 10, 10, TimeUnit.MILLISECONDS);
  }

  public void resolve(final String alert) {
    log.debug("Resolve comment input: {}", alert);
    this.alertCommentsInputQueue.add(alert);
  }

  private void process() {
    try {
      retrieveCommentInputs();
    } catch (Exception e) {
      log.error("Processing of category value failed: ", e);
    }
  }

  private void retrieveCommentInputs() {
    log.trace("Start process single alert with received comment input");
    while (true) {
      final String alertID = this.alertCommentsInputQueue.poll();
      if (alertID == null) {
        break;
      }
      log.debug("Alert id: {} has been determined", alertID);
      final List<CommentInputResponse> commentInputsResponse =
          this.commentInputClient.getCommentInputsResponse(List.of(alertID));

      if (log.isDebugEnabled()) {
        log.debug("Retrieved comments inputs: {}", commentInputsResponse);
      }
      commentInputsResponse.forEach(this::prepareUpdateCommand);
    }
  }

  private void prepareUpdateCommand(CommentInputResponse commentInputResponse) {
    log.debug(
        "Process match comment input response for alert: {} ", commentInputResponse.getAlert());
    long alertId = ResourceName.create(commentInputResponse.getAlert()).alertId();

    final String alertCommentInputsMap =
        converter.convertToJsonString(commentInputResponse.getAlertCommentInput()).orElseThrow();

    this.commentInputClientRepository.store(alertId, alertCommentInputsMap);
  }
}
