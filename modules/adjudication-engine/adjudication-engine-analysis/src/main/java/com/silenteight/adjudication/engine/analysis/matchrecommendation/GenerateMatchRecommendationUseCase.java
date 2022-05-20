package com.silenteight.adjudication.engine.analysis.matchrecommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v3.MatchRecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.engine.analysis.analysis.AnalysisFacade;
import com.silenteight.adjudication.engine.analysis.matchrecommendation.domain.PendingMatch;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.governance.GovernanceFacade;
import com.silenteight.solving.api.v1.BatchSolveAlertsRequest;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
class GenerateMatchRecommendationUseCase {

  private final MatchRecommendationDataAccess dataAccess;
  private final AnalysisFacade analysisFacade;
  private final GovernanceFacade governanceFacade;
  private final CreateMatchRecommendationUseCase createMatchRecommendation;

  List<RecommendationInfo> solveMatches(String analysisName) {
    var recommendationInfoList = new ArrayList<RecommendationInfo>();
    var analysisId = ResourceName.create(analysisName).getLong("analysis");

    do {

      var pendingMatches = dataAccess.selectPendingMatches(analysisId);
      var request = createRequest(analysisId, pendingMatches);

      if (request.isEmpty()) {
        break;
      }

      var response = governanceFacade.batchSolveAlerts(request.get());

      var recommendationInfos = createMatchRecommendation.createMatchRecommendation(analysisId,
          response.getSolutionsList(), pendingMatches);
      recommendationInfoList.addAll(recommendationInfos);
    } while (true);

    return recommendationInfoList;
  }

  private Optional<BatchSolveAlertsRequest> createRequest(
      long analysisId, List<PendingMatch> pendingMatches) {

    if (pendingMatches.isEmpty()) {
      log.debug("No pending matches: analysis=analysis/{}", analysisId);
      return Optional.empty();
    }

    log.debug("Fetched pending matches: analysis=analysis/{}, pendingCount={}",
        analysisId, pendingMatches.size());

    var strategy = analysisFacade.getAnalysisStrategy(analysisId);

    var requestAlerts = pendingMatches.stream().map(PendingMatch::toAlert).collect(toList());

    return Optional.of(BatchSolveAlertsRequest
        .newBuilder()
        .setStrategy(strategy)
        .addAllAlerts(requestAlerts)
        .build());
  }
}
