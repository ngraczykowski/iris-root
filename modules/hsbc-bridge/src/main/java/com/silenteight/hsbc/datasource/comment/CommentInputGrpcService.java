package com.silenteight.hsbc.datasource.comment;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.comments.api.v1.CommentInput;
import com.silenteight.datasource.comments.api.v1.CommentInputServiceGrpc.CommentInputServiceImplBase;
import com.silenteight.datasource.comments.api.v1.MatchCommentInput;
import com.silenteight.datasource.comments.api.v1.StreamCommentInputsRequest;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@GrpcService
@RequiredArgsConstructor
class CommentInputGrpcService extends CommentInputServiceImplBase {

  private final GetCommentInputUseCase getCommentInputUseCase;

  @Override
  public void streamCommentInputs(
      StreamCommentInputsRequest request,
      StreamObserver<CommentInput> responseObserver) {

    var response = mapToCommentInputs(
        getCommentInputUseCase.getInputRequestsResponse(createInputRequest(request)));

    response.forEach(responseObserver::onNext);
    responseObserver.onCompleted();
  }

  private StreamCommentInputsRequestDto createInputRequest(StreamCommentInputsRequest request) {
    return StreamCommentInputsRequestDto.builder()
        .alerts(request.getAlertsList())
        .build();
  }

  private List<CommentInput> mapToCommentInputs(List<CommentInputDto> commentInputsDto) {
    return commentInputsDto.stream()
        .map(commentInputDto -> CommentInput.newBuilder()
            .setAlert(commentInputDto.getAlert())
            .setAlertCommentInput(toStruct(commentInputDto.getAlertCommentInput()))
            .addAllMatchCommentInputs(mapToMatchCommentInputs(commentInputDto))
            .build())
        .collect(toList());
  }

  private static Struct toStruct(Map<String, String> commentData) {
    var builder = Struct.newBuilder();
    commentData.forEach((k,v) -> {
      if (nonNull(v)) {
        builder.putFields(k, Value.newBuilder().setStringValue(v).build());
      }
    });
    return builder.build();
  }

  private List<MatchCommentInput> mapToMatchCommentInputs(CommentInputDto commentInputDto) {
    return commentInputDto.getMatchCommentInputsDto().stream()
        .map(matchCommentInputDto -> MatchCommentInput.newBuilder()
            .setMatch(matchCommentInputDto.getMatch())
            .setCommentInput(Struct.getDefaultInstance())
            .build())
        .collect(toList());
  }
}
