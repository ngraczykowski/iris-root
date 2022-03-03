package com.silenteight.customerbridge.common.batch.ecm;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.common.quartz.EcmAnalystDecision;
import com.silenteight.proto.serp.v1.alert.AnalystSolution;

import java.util.List;

import static com.silenteight.proto.serp.v1.alert.AnalystSolution.ANALYST_OTHER;

@RequiredArgsConstructor
class EcmAnalystDecisionMapper {

  private final List<EcmAnalystDecision> mappedSolutions;

  AnalystSolution mapEcmDecision(String analystDecision) {
    return mappedSolutions
        .stream()
        .filter(s -> s.getText().equals(analystDecision))
        .map(EcmAnalystDecision::getSolution)
        .findFirst()
        .orElse(ANALYST_OTHER);
  }
}
