package com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;
import com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.port.CreateFeatureUseCase;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class CreateFeatureService implements CreateFeatureUseCase {

  private final List<FeatureExtractor> featureExtractors;

  @Override
  public Map<String, List<FeatureInput>> createFeatureInputs(EtlHit etlHit) {
    var featureInputs =
        featureExtractors.stream().map(fe -> fe.createFeatureInputs(etlHit)).collect(toList());
    return Map.of(etlHit.getMatchId(), featureInputs);
  }
}
