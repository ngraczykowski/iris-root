package com.silenteight.adjudication.engine.mock.datasource.v1;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.comments.api.v1.CommentInput;
import com.silenteight.datasource.comments.api.v1.CommentInputServiceGrpc.CommentInputServiceImplBase;
import com.silenteight.datasource.comments.api.v1.StreamCommentInputsRequest;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;

@GrpcService
@Profile("mockdatasource & datasourcev1")
@RequiredArgsConstructor
class MockCommentInputGrpcService extends CommentInputServiceImplBase {

  private final MockCommentsInputUseCase commentsInputUseCase;

  @Override
  public void streamCommentInputs(
      StreamCommentInputsRequest request,
      StreamObserver<CommentInput> responseObserver) {

    var commentInputs = commentsInputUseCase
        .getcommentInputs(request.getAlertsList());
    commentInputs.stream().forEach(responseObserver::onNext);

    responseObserver.onCompleted();
  }
}
