package com.silenteight.adjudication.engine.analysis.commentinput;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.comments.api.v1.CommentInput;
import com.silenteight.datasource.comments.api.v1.CommentInputServiceGrpc.CommentInputServiceBlockingStub;
import com.silenteight.datasource.comments.api.v1.StreamCommentInputsRequest;

import com.google.common.collect.Lists;
import io.grpc.Deadline;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Slf4j
class CommentInputServiceClient {

  private final CommentInputServiceBlockingStub stub;
  private final Duration timeout;

  public List<CommentInput> getCommentInputs(@NotNull StreamCommentInputsRequest request) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (log.isTraceEnabled()) {
      log.trace("Requesting comment inputs: deadline={}, request={}", deadline, request);
    }

    var elements = stub.withDeadline(deadline).streamCommentInputs(request);
    var commentInputs = Lists.newArrayList(elements);

    if (commentInputs.isEmpty()) {
      throw new EmptyCommentInputServiceResponseException(stub.getChannel().authority());
    }

    if (commentInputs.size() < request.getAlertsCount()) {
      log.warn("Received less comment inputs than expected: requestedCount={}, receivedCount={}",
          request.getAlertsCount(), commentInputs.size());
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
