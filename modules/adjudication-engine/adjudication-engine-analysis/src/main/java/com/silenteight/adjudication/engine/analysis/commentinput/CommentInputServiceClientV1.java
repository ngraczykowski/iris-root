package com.silenteight.adjudication.engine.analysis.commentinput;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.commentinput.CommentInputResponse;
import com.silenteight.datasource.comments.api.v1.CommentInput;
import com.silenteight.datasource.comments.api.v1.CommentInputServiceGrpc.CommentInputServiceBlockingStub;
import com.silenteight.datasource.comments.api.v1.StreamCommentInputsRequest;

import com.google.common.collect.Lists;
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
class CommentInputServiceClientV1 implements CommentInputClient {

  private final CommentInputServiceBlockingStub stub;
  private final Duration timeout;

  @Override
  public List<CommentInputResponse> getCommentInputsResponse(List<String> alerts) {
    var request = StreamCommentInputsRequest.newBuilder()
        .addAllAlerts(alerts)
        .build();
    var response = getCommentInputs(request);
    return response.stream().map(CommentInputResponse::fromCommentInputV1).collect(toList());
  }

  List<CommentInput> getCommentInputs(@NotNull StreamCommentInputsRequest request) {
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

  private List<CommentInput> performRequest(StreamCommentInputsRequest request) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (log.isTraceEnabled()) {
      log.trace("Requesting comment inputs: deadline={}, request={}", deadline, request);
    }

    List<CommentInput> commentInputs;
    try {
      commentInputs = Lists.newArrayList(stub.withDeadline(deadline).streamCommentInputs(request));
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
