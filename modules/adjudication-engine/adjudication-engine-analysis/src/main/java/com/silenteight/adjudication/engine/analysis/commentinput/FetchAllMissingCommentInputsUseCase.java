package com.silenteight.adjudication.engine.analysis.commentinput;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.comments.commentinput.AlertCommentInputFacade;
import com.silenteight.adjudication.engine.comments.commentinput.CommentInputResponse;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.sep.base.aspects.metrics.Timed;

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

  @Timed(
      value = "ae.analysis.use_cases",
      extraTags = { "package", "commentinput" },
      histogram = true,
      percentiles = { 0.5, 0.95, 0.99 }
  )
  void fetchAllMissingCommentInputsValues(@NotNull String analysis) {
    var analysisId = ResourceName.create(analysis).getLong("analysis");

    if (log.isDebugEnabled()) {
      log.debug("Fetching missing comment inputs: analysis={}", analysis);
    }

    var totalCount = 0;

    do {
      var result = commentInputDataAccess
          .getMissingCommentInputs(analysisId);

      if (result.isEmpty() || result.get().hasNoAlerts()) {
        break;
      }

      var missingCommentInputs = result.get();

      if (log.isDebugEnabled()) {
        log.debug("Analysis is still missing comment inputs: analysisId={}, missingCount={}",
            analysisId, missingCommentInputs.count());
      }

      var inputs = requestCommentInputs(missingCommentInputs.getAlerts());

      totalCount += inputs.size();

      alertCommentInputFacade.createAlertCommentInputs(inputs);
    } while (true);

    log.info("Fetched missing comment inputs: analysis={}, commentInputCount={}",
        analysis, totalCount);
  }

  private List<CommentInputResponse> requestCommentInputs(@NotNull List<String> alerts) {
    return commentInputClient.getCommentInputsResponse(alerts);
  }
}
