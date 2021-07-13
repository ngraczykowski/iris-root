package com.silenteight.searpayments.bridge.datasource;

import com.silenteight.datasource.comments.api.v1.CommentInput;
import com.silenteight.datasource.comments.api.v1.CommentInputServiceGrpc.CommentInputServiceImplBase;
import com.silenteight.datasource.comments.api.v1.StreamCommentInputsRequest;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
class CommentInputGrpcService extends CommentInputServiceImplBase {

  @Override
  public void streamCommentInputs(
      StreamCommentInputsRequest request,
      StreamObserver<CommentInput> responseObserver) {

    var commentInputs = getCommentInputs(request.getAlertsList());
    commentInputs.forEach(responseObserver::onNext);

    responseObserver.onCompleted();
  }

  private List<CommentInput> getCommentInputs(List<String> alerts) {
    return alerts.stream().map(alert ->
        CommentInput.newBuilder()
            .setAlert(alert)
            .setAlertCommentInput(Struct.newBuilder()
                .putFields("field1", Value.newBuilder().setStringValue("test1-" + alert).build())
                .putFields("field2", Value.newBuilder().setNumberValue(2).build())
                .build())
            .build()
    ).collect(Collectors.toList());
  }
}
