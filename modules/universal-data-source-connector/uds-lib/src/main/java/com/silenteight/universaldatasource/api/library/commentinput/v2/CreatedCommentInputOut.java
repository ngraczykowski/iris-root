package com.silenteight.universaldatasource.api.library.commentinput.v2;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.comments.api.v2.CreatedCommentInput;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class CreatedCommentInputOut {

  String name;
  String alert;

  static CreatedCommentInputOut createFrom(CreatedCommentInput input) {
    return CreatedCommentInputOut.builder()
        .alert(input.getAlert())
        .name(input.getName())
        .build();
  }
}
