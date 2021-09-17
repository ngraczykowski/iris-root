package com.silenteight.universaldatasource.app.commentinput.port.outgoing;

import com.silenteight.datasource.comments.api.v2.CreatedCommentInput;
import com.silenteight.universaldatasource.app.commentinput.model.AlertCommentInput;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public interface CommentInputDataAccess {

  List<CreatedCommentInput> saveAll(List<AlertCommentInput> commentInputs);

  int stream(Collection<String> alerts, Consumer<AlertCommentInput> consumer);

  List<AlertCommentInput> batchGetCommentInputs(List<String> alerts);

}
