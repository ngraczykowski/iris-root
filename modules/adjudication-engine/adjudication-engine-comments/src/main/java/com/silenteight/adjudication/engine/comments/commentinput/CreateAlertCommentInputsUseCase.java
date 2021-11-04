package com.silenteight.adjudication.engine.comments.commentinput;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.commentinput.domain.InsertCommentInputRequest;
import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
class CreateAlertCommentInputsUseCase {

  @NonNull
  private final CommentInputDataAccess dataAccess;
  @NonNull
  private final ProtoMessageToObjectNodeConverter converter;

  @Transactional
  void createAlertCommentInputs(Iterable<CommentInputResponse> commentInputs) {
    commentInputs.forEach(this::saveCommentInput);
  }

  private void saveCommentInput(CommentInputResponse commentInput) {
    var alertId = ResourceName.create(commentInput.getAlert()).getLong("alerts");
    var builder = InsertCommentInputRequest.builder().alertId(alertId);

    // XXX(ahaczewski): How should the invalid comment input be handled?
    converter.convert(commentInput.getAlertCommentInput())
        .ifPresentOrElse(builder::value, () -> log.warn("Failed to convert comment input to JSON"));

    dataAccess.insertCommentInput(builder.build());
  }
}
