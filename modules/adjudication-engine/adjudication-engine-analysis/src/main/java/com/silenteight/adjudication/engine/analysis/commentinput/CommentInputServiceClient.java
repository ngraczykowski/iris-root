package com.silenteight.adjudication.engine.analysis.commentinput;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.comments.api.v1.CommentInput;
import com.silenteight.datasource.comments.api.v1.CommentInputServiceGrpc.CommentInputServiceBlockingStub;
import com.silenteight.datasource.comments.api.v1.StreamCommentInputsRequest;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import io.grpc.Deadline;
import io.grpc.StatusRuntimeException;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Slf4j
class CommentInputServiceClient {

  private final CommentInputServiceBlockingStub stub;
  private final Duration timeout;

  public List<CommentInput> getCommentInputs(@NotNull StreamCommentInputsRequest request) {
    var commentInputs = performRequest(request);

    if (commentInputs.isEmpty()) {
      // FIXME(ahaczewski): Uncomment this exception, instead of hiding Data Source shit.
      //throw new EmptyCommentInputServiceResponseException(stub.getChannel().authority());
    }

    if (commentInputs.size() < request.getAlertsCount()) {
      log.warn("Received less comment inputs than expected: requestedCount={}, receivedCount={}",
          request.getAlertsCount(), commentInputs.size());

      // FIXME(ahaczewski): Remove this return, instead of hiding Data Source shit.
      return IntStream
          .range(0, request.getAlertsCount())
          .mapToObj(idx -> CommentInput.newBuilder().build())
          .collect(Collectors.toList());
    }

    return commentInputs;
  }

  private List<CommentInput> performRequest(StreamCommentInputsRequest request) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (log.isTraceEnabled()) {
      log.trace("Requesting comment inputs: deadline={}, request={}", deadline, request);
    }

    Iterator<CommentInput> elements;
    try {
      elements = stub.withDeadline(deadline).streamCommentInputs(request);
    } catch (StatusRuntimeException status) {
      // FIXME(ahaczewski): Remove that mockup once data source is fixed.
      log.warn("Oh well, data source failed to tell us comment inputs... we'll figuring it"
          + " out ourselves");
      elements = Iterators.forArray(new CommentInput[0]);
    }

    var commentInputs = Lists.newArrayList(elements);

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
