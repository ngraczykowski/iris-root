package com.silenteight.universaldatasource.app.feature.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchFeatureInputResponse;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchGetFeatureInputUseCase;
import com.silenteight.universaldatasource.app.feature.port.outgoing.FeatureDataAccess;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Service
class BatchGetFeatureInputService implements BatchGetFeatureInputUseCase {

  private final FeatureDataAccess dataAccess;
  private final FeatureMapperFactory featureMapperFactory;

  @Timed(value = "uds.comment-input.use_cases", extraTags = { "action", "batchGetFeature" })
  @Override
  public void batchGetFeatureInput(
      Collection<String> matchNames, Collection<String> featureNames,
      Consumer<BatchFeatureInputResponse> consumer) {

    log.debug("Streaming feature inputs");

    var featureInputsCount = dataAccess.stream(matchNames, featureNames,
        matchFeatureOutput -> consumer.accept(featureMapperFactory
            .get(matchFeatureOutput.getAgentInputType())
            .map(matchFeatureOutput)));

    log.info("Finished streaming feature inputs: featureInputsCount={}", featureInputsCount);
  }
}
