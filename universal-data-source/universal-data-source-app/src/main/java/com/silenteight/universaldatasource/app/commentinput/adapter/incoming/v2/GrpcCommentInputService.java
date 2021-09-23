package com.silenteight.universaldatasource.app.commentinput.adapter.incoming.v2;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.comments.api.v2.BatchCreateCommentInputRequest;
import com.silenteight.datasource.comments.api.v2.BatchCreateCommentInputResponse;
import com.silenteight.datasource.comments.api.v2.BatchGetAlertsCommentInputsRequest;
import com.silenteight.datasource.comments.api.v2.BatchGetAlertsCommentInputsResponse;
import com.silenteight.datasource.comments.api.v2.CommentInputServiceGrpc.CommentInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@RequiredArgsConstructor
@GrpcService
class GrpcCommentInputService extends CommentInputServiceImplBase {

  private final CommentInputAdapter commentInputAdapter;

  @Override
  public void batchCreateCommentInput(
      BatchCreateCommentInputRequest request,
      StreamObserver<BatchCreateCommentInputResponse> responseObserver) {

    responseObserver.onNext(commentInputAdapter.batchCreateCommentInput(request));
    responseObserver.onCompleted();
  }

  @Override
  public void batchGetAlertsCommentInputs(
      BatchGetAlertsCommentInputsRequest request,
      StreamObserver<BatchGetAlertsCommentInputsResponse> responseObserver) {

    responseObserver.onNext(commentInputAdapter.batchGetAlertsCommentInputs(request));
    responseObserver.onCompleted();
  }
}
