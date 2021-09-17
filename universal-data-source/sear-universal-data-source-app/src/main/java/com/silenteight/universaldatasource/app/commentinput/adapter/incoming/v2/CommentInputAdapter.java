package com.silenteight.universaldatasource.app.commentinput.adapter.incoming.v2;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.comments.api.v2.BatchCreateCommentInputRequest;
import com.silenteight.datasource.comments.api.v2.BatchCreateCommentInputResponse;
import com.silenteight.datasource.comments.api.v2.BatchGetAlertsCommentInputsRequest;
import com.silenteight.datasource.comments.api.v2.BatchGetAlertsCommentInputsResponse;
import com.silenteight.universaldatasource.app.commentinput.port.incoming.BatchGetAlertsCommentInputsUseCase;
import com.silenteight.universaldatasource.app.commentinput.port.incoming.CreateCommentInputsUseCase;

import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class CommentInputAdapter {

  private final BatchGetAlertsCommentInputsUseCase batchGetAlertsCommentInputsUseCase;

  private final CreateCommentInputsUseCase createCommentInputsUseCase;

  BatchGetAlertsCommentInputsResponse batchGetAlertsCommentInputs(
      BatchGetAlertsCommentInputsRequest request) {
    return batchGetAlertsCommentInputsUseCase.batchGetAlertsCommentInputs(request.getAlertsList());
  }

  BatchCreateCommentInputResponse batchCreateCommentInput(BatchCreateCommentInputRequest request) {
    return createCommentInputsUseCase.addCommentInputs(request.getCommentInputsList());
  }
}
