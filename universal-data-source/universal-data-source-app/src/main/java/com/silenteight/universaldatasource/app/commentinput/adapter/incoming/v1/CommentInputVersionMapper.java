package com.silenteight.universaldatasource.app.commentinput.adapter.incoming.v1;

import com.silenteight.datasource.comments.api.v1.CommentInput;
import com.silenteight.datasource.comments.api.v1.MatchCommentInput;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
class CommentInputVersionMapper {

  CommentInput map(
      com.silenteight.datasource.comments.api.v2.CommentInput commentInput) {

    var matchCommentInputList =
        getMatchCommentInputList(commentInput.getMatchCommentInputsList());

    return CommentInput.newBuilder()
        .setAlert(commentInput.getAlert())
        .setAlertCommentInput(commentInput.getAlertCommentInput())
        .addAllMatchCommentInputs(matchCommentInputList)
        .build();
  }

  private static List<MatchCommentInput> getMatchCommentInputList(
      List<com.silenteight.datasource.comments.api.v2.MatchCommentInput> matchCommentInputs) {
    return matchCommentInputs.stream()
        .map(m -> MatchCommentInput.newBuilder()
            .setMatch(m.getMatch())
            .setCommentInput(m.getCommentInput())
            .build())
        .collect(Collectors.toList());
  }

}
