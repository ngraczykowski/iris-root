package com.silenteight.adjudication.engine.solving.application.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.commentinput.CommentInputClient;
import com.silenteight.adjudication.engine.comments.commentinput.CommentInputResponse;
import com.silenteight.adjudication.engine.comments.commentinput.MatchCommentInputResponse;
import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInputClientRepository;

import com.hazelcast.collection.IQueue;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
class CommentInputResolveProcess {

  private final CommentInputClient commentInputClient;

  private final ProtoMessageToObjectNodeConverter converter;
  private final CommentInputClientRepository commentInputClientRepository;
  private final IQueue<String> alertCommentsInputQueue;

  CommentInputResolveProcess(
      final CommentInputClient commentInputClient,
      final ProtoMessageToObjectNodeConverter converter,
      final CommentInputClientRepository commentInputClientRepository,
      final ScheduledExecutorService scheduledExecutorService,
      final IQueue<String> alertCommentsInputQueue
  ) {
    this.commentInputClient = commentInputClient;
    this.converter = converter;
    this.commentInputClientRepository = commentInputClientRepository;
    this.alertCommentsInputQueue = alertCommentsInputQueue;
    scheduledExecutorService.scheduleAtFixedRate(this::process, 500, 500, TimeUnit.MILLISECONDS);
  }

  public void resolve(final String alert) {
    log.debug("Resolve comment input: {}", alert);
    this.alertCommentsInputQueue.add(alert);
  }


  private void prepareUpdateCommand(MatchCommentInputResponse matchCommentInputResponse) {
    log.debug("Process match comment input response: {} ", matchCommentInputResponse);
    long alertId =
        ResourceName.create(matchCommentInputResponse.getMatch()).alertId();

    final String commentInputsMap = converter
        .convertToJsonString(matchCommentInputResponse.getCommentInput())
        .orElseThrow();

    this.commentInputClientRepository.store(alertId, commentInputsMap);
  }

  private void process() {
    log.debug("Start process single alert ");
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
      commentInputsResponse
          .stream()
          .flatMap(p -> p.getMatchCommentInput().stream())
          .forEach(this::prepareUpdateCommand);
    }
  }
}
