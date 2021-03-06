package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

@RequiredArgsConstructor
@Service
class FindAnalysisByPendingMatchesUseCase {

  private final AnalysisDataAccess analysisDataAccess;

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "analysis" }, percentiles = {
      0.5, 0.95, 0.99 }, histogram = true)
  List<String> findAnalysisByPendingMatches(List<String> matches) {
    var matchIds = matches
        .stream()
        .map(matchName -> ResourceName.create(matchName).getLong("matches"))
        .collect(toUnmodifiableList());

    return analysisDataAccess.findByPendingRecommendationMatchIds(matchIds)
        .stream()
        .map(analysisId -> "analysis/" + analysisId)
        .collect(toUnmodifiableList());
  }
}
