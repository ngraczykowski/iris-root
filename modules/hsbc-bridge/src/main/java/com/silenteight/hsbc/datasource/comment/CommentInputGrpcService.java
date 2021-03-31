package com.silenteight.hsbc.datasource.comment;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.comments.api.v1.CommentInput;
import com.silenteight.datasource.comments.api.v1.CommentInputServiceGrpc.CommentInputServiceImplBase;
import com.silenteight.datasource.comments.api.v1.MatchCommentInput;
import com.silenteight.datasource.comments.api.v1.StreamCommentInputsRequest;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import java.util.List;

import static java.util.stream.Collectors.toList;

@GRpcService
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
//          .setAlertCommentInput() TODO: Struct need to be add here
            .addAllMatchCommentInputs(mapToMatchCommentInputs(commentInputDto))
            .build())
        .collect(toList());
  }

  private List<MatchCommentInput> mapToMatchCommentInputs(CommentInputDto commentInputDto) {
    return commentInputDto.getMatchCommentInputsDto().stream()
        .map(matchCommentInputDto -> MatchCommentInput.newBuilder()
            .setMatch(matchCommentInputDto.getMatch())
//          .setCommentInput() TODO: Struct need to be add here
            .build())
        .collect(toList());
  }
}
