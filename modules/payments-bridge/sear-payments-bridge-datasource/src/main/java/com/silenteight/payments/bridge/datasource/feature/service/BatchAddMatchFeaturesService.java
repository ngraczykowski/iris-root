package com.silenteight.payments.bridge.datasource.feature.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.datasource.feature.model.MatchFeatureInput;
import com.silenteight.payments.bridge.datasource.feature.port.incoming.AddMatchFeaturesRequest;
import com.silenteight.payments.bridge.datasource.feature.port.incoming.AddMatchFeaturesRequest.FeatureAgentInput;
import com.silenteight.payments.bridge.datasource.feature.port.incoming.AddMatchFeaturesRequest.Match;
import com.silenteight.payments.bridge.datasource.feature.port.incoming.BatchAddMatchFeaturesUseCase;
import com.silenteight.payments.bridge.datasource.feature.port.outgoing.FeatureDataAccess;
import com.silenteight.sep.base.common.protocol.MessageRegistry;

import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
class BatchAddMatchFeaturesService implements BatchAddMatchFeaturesUseCase {

  private static final int MAX_BATCH_SIZE = 4096;

  private final MessageRegistry messageRegistry;
  private final FeatureDataAccess dataAccess;

  @Override
  public void batchAddMatchFeatures(Collection<AddMatchFeaturesRequest> requests) {
    List<MatchFeatureInput> matchFeatureInputs = new ArrayList<>();

    // NOTE(jgajewski): For those afraid of such a loop nesting: the input requests collection
    //  will have very small number of elements.
    for (var addMatchFeaturesRequest : requests) {
      for (var match : addMatchFeaturesRequest.getMatches()) {
        for (var featureAgentInput : match.getInputs()) {
          matchFeatureInputs.add(createFeatureInput(match, featureAgentInput));

          if (matchFeatureInputs.size() >= MAX_BATCH_SIZE) {
            dataAccess.saveAll(matchFeatureInputs);
            matchFeatureInputs.clear();
          }
        }
      }
    }

    dataAccess.saveAll(matchFeatureInputs);
  }

  private MatchFeatureInput createFeatureInput(Match match, FeatureAgentInput featureAgentInput) {
    try {
      return map(match, featureAgentInput);
    } catch (InvalidProtocolBufferException e) {
      throw new MatchFeatureInputMappingException(e);
    }
  }

  private MatchFeatureInput map(
      Match match, FeatureAgentInput featureAgentInput) throws InvalidProtocolBufferException {

    var message = featureAgentInput.getAgentInput();
    var agentInput = messageRegistry.toJson(message);

    return new MatchFeatureInput(
        match.getMatchName(), featureAgentInput.getFeatureName(),
        message.getClass().getSimpleName(), agentInput);
  }

  private static class MatchFeatureInputMappingException extends RuntimeException {

    private static final long serialVersionUID = -7457823269043373654L;

    MatchFeatureInputMappingException(Throwable cause) {
      super("Unable to map feature input", cause);
    }
  }
}
