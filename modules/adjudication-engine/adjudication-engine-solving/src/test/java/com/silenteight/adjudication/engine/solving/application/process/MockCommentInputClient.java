/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.analysis.commentinput.CommentInputClient;
import com.silenteight.adjudication.engine.comments.commentinput.CommentInputResponse;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.silenteight.adjudication.engine.solving.application.process.CommentInputFixture.COMMENT_INPUT_KEY;
import static com.silenteight.adjudication.engine.solving.application.process.CommentInputFixture.COMMENT_INPUT_VALUE;

class MockCommentInputClient implements CommentInputClient {

  @Override
  public List<CommentInputResponse> getCommentInputsResponse(List<String> alerts) {
    return alerts.stream()
        .map(
            a ->
                CommentInputResponse.builder()
                    .alert(a)
                    .alertCommentInput(
                        Struct.newBuilder()
                            .putAllFields(
                                Map.of(
                                    COMMENT_INPUT_KEY,
                                    Value.newBuilder().setStringValue(COMMENT_INPUT_VALUE).build()))
                            .build())
                    .build())
        .collect(Collectors.toList());
  }
}
