/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.qco.domain.model.MatchSolution;
import com.silenteight.iris.qco.domain.model.QcoRecommendationMatch;
import com.silenteight.iris.qco.domain.port.outgoing.QcoOverriddenRecommendationService;
import com.silenteight.iris.qco.domain.port.outgoing.QcoWarehouseReportAdapter;

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
    log.info("The processed match={} has been registered with {}",
        match.toString(), matchSolution.toString());
  }
}
