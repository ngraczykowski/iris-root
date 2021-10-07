package com.silenteight.adjudication.engine.analysis.commentinput;

import com.silenteight.adjudication.engine.comments.commentinput.CommentInputResponse;

import java.util.List;

interface CommentInputClient {

  List<CommentInputResponse> getCommentInputsResponse(List<String> alerts);
}
