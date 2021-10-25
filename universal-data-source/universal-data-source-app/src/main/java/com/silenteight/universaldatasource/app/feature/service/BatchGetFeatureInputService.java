package com.silenteight.universaldatasource.app.feature.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.universaldatasource.app.feature.model.BatchFeatureRequest;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchFeatureInputResponse;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchGetFeatureInputUseCase;
import com.silenteight.universaldatasource.app.feature.port.outgoing.FeatureDataAccess;

import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Service
class BatchGetFeatureInputService implements BatchGetFeatureInputUseCase {

  private final FeatureDataAccess dataAccess;
  private final FeatureMapperFactory featureMapperFactory;

  @Timed(value = "uds.feature.use_cases", extraTags = { "action", "batchGetFeature" })
  @Override
  public void batchGetFeatureInput(
      BatchFeatureRequest batchFeatureRequest,
      Consumer<BatchFeatureInputResponse> consumer) {

    if (log.isDebugEnabled()) {
      var matches = batchFeatureRequest.getMatches();

      log.debug(
          "Streaming feature inputs: agentInputType={}, features={}, matchCount={}"
              + ", firstTenMatches={}",
          batchFeatureRequest.getAgentInputType(), batchFeatureRequest.getFeatures(),
          matches.size(), matches.subList(0, Math.min(10, matches.size())));
    }

    int featuresCount = dataAccess.stream(
        batchFeatureRequest,
        matchFeatureOutput -> consumer.accept(featureMapperFactory
            .get(matchFeatureOutput.getAgentInputType())
            .map(matchFeatureOutput)));

    if (log.isDebugEnabled()) {
      log.debug("Finished streaming feature inputs: agentInputType={}, featuresCount={}",
          batchFeatureRequest.getAgentInputType(), featuresCount);
    }
  }
}
