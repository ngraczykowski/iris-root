package com.silenteight.universaldatasource.app.commentinput.adapter.incoming.v1;

import com.silenteight.datasource.comments.api.v1.CommentInput;
import com.silenteight.datasource.comments.api.v1.CommentInputServiceGrpc.CommentInputServiceImplBase;
import com.silenteight.datasource.comments.api.v1.StreamCommentInputsRequest;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Qualifier;

@GrpcService
class GrpcCommentInputServiceV1 extends CommentInputServiceImplBase {

  private final CommentInputAdapter commentInputAdapter;

  GrpcCommentInputServiceV1(
      @Qualifier("commentInputAdapterV1") CommentInputAdapter commentInputAdapter) {
    this.commentInputAdapter = commentInputAdapter;
  }

  @Override
  public void streamCommentInputs(
      StreamCommentInputsRequest request,
      StreamObserver<CommentInput> responseObserver) {

    commentInputAdapter.streamCommentInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
