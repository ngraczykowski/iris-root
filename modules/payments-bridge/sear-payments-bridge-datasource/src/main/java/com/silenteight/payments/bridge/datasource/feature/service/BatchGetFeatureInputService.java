package com.silenteight.payments.bridge.datasource.feature.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.resource.ResourceName;
import com.silenteight.payments.bridge.datasource.feature.port.incoming.BatchFeatureInputResponse;
import com.silenteight.payments.bridge.datasource.feature.port.incoming.BatchGetFeatureInputUseCase;
import com.silenteight.payments.bridge.datasource.feature.port.outgoing.FeatureDataAccess;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
class BatchGetFeatureInputService implements BatchGetFeatureInputUseCase {

  private final FeatureDataAccess dataAccess;
  private final FeatureMapperFactory featureMapperFactory;

  @Override
  public void batchGetFeatureInput(
      Collection<String> matchNames, Collection<String> featureNames,
      Consumer<BatchFeatureInputResponse> consumer) {

    dataAccess.stream(
        getMatchList(matchNames, "matches"),
        getMatchList(featureNames, "features"),
        matchFeatureOutput -> consumer.accept(featureMapperFactory
            .get(matchFeatureOutput.getAgentInputType())
            .map(matchFeatureOutput)));
  }

  private static List<String> getMatchList(Collection<String> requests, String key) {
    return requests.stream()
        .map(r -> ResourceName.create(r).get(key))
        .collect(Collectors.toList());
  }
}
