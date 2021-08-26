package com.silenteight.payments.bridge.datasource.commentinput.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.comments.api.v1.CommentInput;
import com.silenteight.datasource.comments.api.v1.CommentInputServiceGrpc.CommentInputServiceImplBase;
import com.silenteight.datasource.comments.api.v1.StreamCommentInputsRequest;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcCommentInputService extends CommentInputServiceImplBase {

  private final CommentInputService commentInputService;

  @Override
  public void streamCommentInputs(
      StreamCommentInputsRequest request,
      StreamObserver<CommentInput> responseObserver) {

    // XXX: See note in GrpcNameInputService for the implementation guide.

    // TODO(ahaczewski): Implement streamCommentInputs.
    responseObserver.onError(Status.UNIMPLEMENTED.asRuntimeException());
  }
}
