/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.ecm;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.decision.Decision.AnalystSolution;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.quartz.EcmAnalystDecision;

import java.util.List;

@RequiredArgsConstructor
class EcmAnalystDecisionMapper {

  private final List<EcmAnalystDecision> mappedSolutions;

  AnalystSolution mapEcmDecision(String analystDecision) {
    return mappedSolutions
        .stream()
        .filter(s -> s.getText().equals(analystDecision))
        .map(EcmAnalystDecision::getSolution)
        .findFirst()
        .orElse(AnalystSolution.ANALYST_OTHER);
  }
}
