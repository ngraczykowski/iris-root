package com.silenteight.adjudication.engine.analysis.commentinput;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.commentinput.AlertCommentInputFacade;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.datasource.comments.api.v1.CommentInput;
import com.silenteight.datasource.comments.api.v1.StreamCommentInputsRequest;

import org.springframework.stereotype.Service;

import java.util.List;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Service
@Slf4j
class FetchAllMissingCommentInputsUseCase {

  private final CommentInputDataAccess commentInputDataAccess;
  private final CommentInputClient commentInputClient;
  private final AlertCommentInputFacade alertCommentInputFacade;

  void fetchAllMissingCommentInputsValues(@NotNull String analysis) {
    var analysisId = ResourceName.create(analysis).getLong("analysis");

    if (log.isDebugEnabled()) {
      log.debug(
          "Handled pending recommendations: analysis={}", analysis);
    }

    do {
      var result = commentInputDataAccess
          .getMissingCommentInputs(analysisId);

      if (result.isEmpty() || result.get().hasNoAlerts()) {
        break;
      }

      var missingCommentInputs = result.get();

      if (log.isTraceEnabled()) {
        log.trace("Analysis is still missing comment inputs: analysisId={}, missingCount={}",
            analysisId, missingCommentInputs.count());
      }

      var inputs = requestCommentInputs(missingCommentInputs.getAlerts());

      alertCommentInputFacade.createAlertCommentInputs(inputs);
    } while (true);
  }

  private List<CommentInput> requestCommentInputs(@NotNull List<String> alerts) {
    return commentInputClient.getCommentInputs(StreamCommentInputsRequest.newBuilder()
        .addAllAlerts(alerts)
        .build());
  }
}
