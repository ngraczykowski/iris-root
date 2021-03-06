package com.silenteight.adjudication.engine.analysis.commentinput;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.commentinput.CommentInputResponse;
import com.silenteight.datasource.comments.api.v2.BatchGetAlertsCommentInputsRequest;
import com.silenteight.datasource.comments.api.v2.CommentInput;
import com.silenteight.datasource.comments.api.v2.CommentInputServiceGrpc.CommentInputServiceBlockingStub;
import com.silenteight.sep.base.aspects.metrics.Timed;

import io.grpc.Deadline;
import io.grpc.StatusRuntimeException;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.validation.constraints.NotNull;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class CommentInputServiceClientV2 implements CommentInputClient {

  private final CommentInputServiceBlockingStub stub;
  private final Duration timeout;

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true, extraTags = {"version", "v2"})
  public List<CommentInputResponse> getCommentInputsResponse(List<String> alerts) {
    var request = BatchGetAlertsCommentInputsRequest.newBuilder()
        .addAllAlerts(alerts)
        .build();
    var response = getCommentInputs(request);
    return response.stream().map(CommentInputResponse::fromCommentInputV2).collect(toList());
  }

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true, extraTags = {"version", "v2"})
  List<CommentInput> getCommentInputs(@NotNull BatchGetAlertsCommentInputsRequest request) {
    var commentInputs = performRequest(request);

    if (commentInputs.isEmpty()) {
      // FIXME(ahaczewski): Uncomment this exception, instead of hiding Data Source shit.
      //throw new EmptyCommentInputServiceResponseException(stub.getChannel().authority());
    }

    if (commentInputs.size() < request.getAlertsCount()) {
      log.warn("Received less comment inputs than expected: requestedCount={}, receivedCount={}",
          request.getAlertsCount(), commentInputs.size());

      // FIXME(ahaczewski): Remove this return, instead of hiding Data Source shit.
      return request.getAlertsList()
          .stream()
          .map(alert -> CommentInput.newBuilder().setAlert(alert).build())
          .collect(toList());
    }

    return commentInputs;
  }

  private List<CommentInput> performRequest(BatchGetAlertsCommentInputsRequest request) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (log.isTraceEnabled()) {
      log.trace("Requesting comment inputs: deadline={}, request={}", deadline, request);
    }

    List<CommentInput> commentInputs;
    try {
      commentInputs =
          stub.withDeadline(deadline).batchGetAlertsCommentInputs(request).getCommentInputsList();
    } catch (StatusRuntimeException status) {
      // FIXME(ahaczewski): Remove that mockup once data source is fixed.
      log.warn("Oh well, data source failed to tell us comment inputs... we'll figuring it"
          + " out ourselves");
      commentInputs = emptyList();
    }

    if (log.isTraceEnabled()) {
      log.trace("Received comment inputs: response={}", commentInputs);
    }

    return commentInputs;
  }

  private static final class EmptyCommentInputServiceResponseException extends RuntimeException {

    private static final long serialVersionUID = 7750138753922426634L;

    EmptyCommentInputServiceResponseException(String authority) {
      super("Received empty response from Comment Input Service at [" + authority + "]");
    }
  }
}
