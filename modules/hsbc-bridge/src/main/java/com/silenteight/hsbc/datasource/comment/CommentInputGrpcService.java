package com.silenteight.hsbc.datasource.comment;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.comments.api.v2.BatchGetAlertsCommentInputsRequest;
import com.silenteight.datasource.comments.api.v2.BatchGetAlertsCommentInputsResponse;
import com.silenteight.datasource.comments.api.v2.CommentInput;
import com.silenteight.datasource.comments.api.v2.CommentInputServiceGrpc.CommentInputServiceImplBase;
import com.silenteight.datasource.comments.api.v2.MatchCommentInput;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
class CommentInputGrpcService extends CommentInputServiceImplBase {

  private final GetCommentInputUseCase getCommentInputUseCase;

  @Override
  public void batchGetAlertsCommentInputs(
      BatchGetAlertsCommentInputsRequest request,
      StreamObserver<BatchGetAlertsCommentInputsResponse> responseObserver) {
    responseObserver.onNext(createResponse(request));
    responseObserver.onCompleted();
  }

  private BatchGetAlertsCommentInputsResponse createResponse(BatchGetAlertsCommentInputsRequest request) {
    var commentInputs =
        getCommentInputUseCase.getInputRequestsResponse(createInputRequest(request));

    return BatchGetAlertsCommentInputsResponse.newBuilder()
        .addAllCommentInputs(mapToCommentInputs(commentInputs))
        .build();
  }

  private BatchGetAlertsCommentInputsRequestDto createInputRequest(
      BatchGetAlertsCommentInputsRequest request) {
    return BatchGetAlertsCommentInputsRequestDto.builder()
        .alerts(request.getAlertsList())
        .build();
  }

  private List<CommentInput> mapToCommentInputs(List<CommentInputDto> commentInputsDto) {
    return commentInputsDto.stream()
        .map(commentInputDto -> CommentInput.newBuilder()
            .setName(commentInputDto.getName())
            .setAlert(commentInputDto.getAlert())
            .setAlertCommentInput(toStruct(commentInputDto.getAlertCommentInput()))
            .addAllMatchCommentInputs(mapToMatchCommentInputs(commentInputDto))
            .build())
        .collect(Collectors.toList());
  }

  private static Struct toStruct(Map<String, String> commentData) {
    var builder = Struct.newBuilder();
    commentData.forEach((k, v) -> {
      if (Objects.nonNull(v)) {
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
        .collect(Collectors.toList());
  }
}
