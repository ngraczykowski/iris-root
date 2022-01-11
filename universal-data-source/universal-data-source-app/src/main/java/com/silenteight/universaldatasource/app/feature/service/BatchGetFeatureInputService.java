package com.silenteight.universaldatasource.app.feature.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.universaldatasource.app.feature.model.BatchFeatureRequest;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput.MatchInput;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchFeatureInputResponse;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchGetFeatureInputUseCase;
import com.silenteight.universaldatasource.app.feature.port.outgoing.FeatureDataAccess;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    var matches = batchFeatureRequest.getMatches();

    if (log.isDebugEnabled()) {
      log.debug(
          "Streaming feature inputs: agentInputType={}, features={}, matchCount={}"
              + ", firstTenMatches={}",
          batchFeatureRequest.getAgentInputType(), batchFeatureRequest.getFeatures(),
          matches.size(), matches.subList(0, Math.min(10, matches.size())));
    }

    var featureOutputConsumer = FeatureOutputConsumer.builder()
        .featureMapperFactory(featureMapperFactory)
        .agentInputType(batchFeatureRequest.getAgentInputType())
        .matches(new HashSet<>(matches))
        .features(batchFeatureRequest.getFeatures())
        .consumer(consumer)
        .build();

    int featuresCount = dataAccess.stream(
        batchFeatureRequest,
        featureOutputConsumer::consumeFeatureOutput);

    featureOutputConsumer.mockUnseenMatchOutputs();

    if (log.isDebugEnabled()) {
      log.debug("Finished streaming feature inputs: agentInputType={}, featuresCount={}",
          batchFeatureRequest.getAgentInputType(), featuresCount);
    }
  }

  @RequiredArgsConstructor
  @Builder
  static class FeatureOutputConsumer {

    private final FeatureMapperFactory featureMapperFactory;
    private final String agentInputType;
    private final Set<String> matches;
    private final List<String> features;
    private final Consumer<BatchFeatureInputResponse> consumer;

    void consumeFeatureOutput(MatchFeatureOutput matchFeatureOutput) {
      matches.removeAll(
          matchFeatureOutput.getMatchInputs().stream().map(MatchInput::getMatch).collect(
              Collectors.toSet()));

      var featureInputResponse = featureMapperFactory
          .get(matchFeatureOutput.getAgentInputType())
          .map(matchFeatureOutput);

      consumer.accept(featureInputResponse);
    }

    void mockUnseenMatchOutputs() {
      log.debug(
          "Sending mocked unseen matches - all features inputs are mocked (matches size: {}).",
          matches.size());
      var emptyResponse =
          featureMapperFactory.get(agentInputType).createEmptyResponse(matches, features);
      consumer.accept(emptyResponse);
    }
  }
}
