package com.silenteight.qco.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.qco.domain.model.MatchSolution;
import com.silenteight.qco.domain.model.QcoRecommendationMatch;
import com.silenteight.qco.domain.port.outgoing.QcoOverriddenRecommendationService;
import com.silenteight.qco.domain.port.outgoing.QcoWarehouseReportAdapter;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class ProcessedMatchesRegister {

  private final QcoOverriddenRecommendationService recommendationService;
  private final QcoWarehouseReportAdapter reportsSenderAdapter;

  @Async("qcoAsyncExecutor")
  public void register(QcoRecommendationMatch match, MatchSolution matchSolution) {
    recommendationService.storeQcoOverriddenRecommendation(match, matchSolution);
    reportsSenderAdapter.send(match, matchSolution);
    log.info("The processed match={} has been registered", match.matchName());
  }
}
