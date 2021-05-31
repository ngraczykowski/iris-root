package com.silenteight.adjudication.engine.analysis.commentinput;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.comments.api.v1.CommentInput;
import com.silenteight.datasource.comments.api.v1.CommentInputServiceGrpc.CommentInputServiceBlockingStub;
import com.silenteight.datasource.comments.api.v1.StreamCommentInputsRequest;

import com.google.common.collect.Lists;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Service
@Slf4j
class CommentInputClient {

  @GrpcClient("data-source")
  private CommentInputServiceBlockingStub stub;

  public List<CommentInput> getCommentInputs(
      @NotNull StreamCommentInputsRequest request) {
    return Lists.newArrayList(stub.streamCommentInputs(request));
  }
}
