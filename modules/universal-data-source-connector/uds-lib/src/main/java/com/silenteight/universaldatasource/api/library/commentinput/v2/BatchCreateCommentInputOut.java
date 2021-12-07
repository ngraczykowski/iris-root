package com.silenteight.universaldatasource.api.library.commentinput.v2;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.comments.api.v2.BatchCreateCommentInputResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class BatchCreateCommentInputOut {

  @Builder.Default
  List<CreatedCommentInputOut> createdCommentInputs = List.of();

  static BatchCreateCommentInputOut createFrom(BatchCreateCommentInputResponse response) {
    return BatchCreateCommentInputOut.builder()
        .createdCommentInputs(response.getCreatedCommentInputsList().stream()
            .map(CreatedCommentInputOut::createFrom)
            .collect(Collectors.toList())
        )
        .build();
  }
}
