package com.silenteight.adjudication.engine.analysis.recommendation;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.GenerateCommentsRequest;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.GenerateCommentsResponse;

import org.springframework.stereotype.Service;

@Service
class GenerateCommentsUseCase {

  /*
   * Example comment:
   *
   *    S8 recommended action: Manual Investigation
   *    False Positive hits:
   *      CL09000597: Alerted Party's name (JAMILLY ARAGÃO) is an exact match with
   *        Watchlist Party name (Prof JAMILLY ARAGÃO). Alerted Party's DOB (1959-10-15)
   *        does not match Watchlist Party DOB/YOB (1964-07-09).
   *    Manual Investigation hits:
   *      CL08919459
   *      DB00011220
   *    Obsolete hits (no further action required):
   *      CL12122787
   */
  GenerateCommentsResponse generateComments(GenerateCommentsRequest request) {
    return null;
  }
}
