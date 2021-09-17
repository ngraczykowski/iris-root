package com.silenteight.universaldatasource.app.commentinput.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AlertCommentInput {

  String commentInputId;

  String alert;

  String commentInput;

  String matchCommentInputs;
}
