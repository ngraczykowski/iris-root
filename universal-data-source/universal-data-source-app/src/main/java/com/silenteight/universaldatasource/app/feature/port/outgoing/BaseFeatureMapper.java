package com.silenteight.universaldatasource.app.feature.port.outgoing;


import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput.AgentInput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput.MatchInput;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchFeatureInputResponse;
import com.silenteight.universaldatasource.common.protobuf.JsonToStructConverter;

import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseFeatureMapper<T extends Message> implements FeatureMapper {

  private final String className;

  protected BaseFeatureMapper(Class<T> featureInputType) {
    this.className = featureInputType.getCanonicalName();
  }

  @Override
  public String getType() {
    return className;
  }

  @Override
  public BatchFeatureInputResponse map(MatchFeatureOutput matchFeatureOutput) {

    var batchResponseBuilder = createBatchResponseBuilder();
    var nameInputField = batchResponseBuilder.getDescriptorForType().findFieldByNumber(1);

    for (MatchInput matchInput : matchFeatureOutput.getMatchInputs()) {

      var inputBuilder = batchResponseBuilder.newBuilderForField(nameInputField);

      var matchField = inputBuilder.getDescriptorForType().findFieldByNumber(1);
      inputBuilder.setField(matchField, matchInput.getMatch());

      var featureInputsField = inputBuilder.getDescriptorForType().findFieldByNumber(2);

      for (AgentInput agentInput : matchInput.getAgentInputs()) {
        var featureInputBuilder = inputBuilder.newBuilderForField(featureInputsField);
        JsonToStructConverter.map(featureInputBuilder, agentInput.getAgentInputJson());
        inputBuilder.addRepeatedField(featureInputsField, featureInputBuilder.build());
      }

      batchResponseBuilder.addRepeatedField(nameInputField, inputBuilder.build());
    }

    var listOfMissingFeatures = createMissingFeaturesResponse(matchFeatureOutput);
    listOfMissingFeatures.forEach(c -> batchResponseBuilder.addRepeatedField(nameInputField, c));

    return new BatchFeatureInputResponse(batchResponseBuilder.build());
  }

  private List<Message> createMissingFeaturesResponse(MatchFeatureOutput matchFeatureOutput) {
    var batchFeatureRequest = matchFeatureOutput.getBatchFeatureRequest();

    var matchInputs = matchFeatureOutput.getMatchInputs();

    var missingMatches = batchFeatureRequest.getMatches().stream()
        .filter(m -> !contains(matchInputs, m))
        .collect(Collectors.toList());

    var listOfMissingFeatures = new ArrayList<Message>();
    for (var feature : batchFeatureRequest.getFeatures()) {
      listOfMissingFeatures.addAll(createMissingFeatures(missingMatches, feature));
    }
    return listOfMissingFeatures;
  }

  private List<Message> createMissingFeatures(List<String> missingMatches, String feature) {
    return missingMatches.stream()
        .map(m -> {
          var inputBuilder = createInputBuilder();
          var matchField = inputBuilder.getDescriptorForType().findFieldByNumber(1);
          var inputField = inputBuilder.getDescriptorForType().findFieldByNumber(2);
          inputBuilder.setField(matchField, m);
          var defaultFeatureInput = getDefaultFeatureInput();
          var featureField = defaultFeatureInput.getDescriptorForType().findFieldByNumber(1);
          defaultFeatureInput.setField(featureField, feature);
          inputBuilder.addRepeatedField(inputField, defaultFeatureInput.build());
          return inputBuilder.build();
        })
        .collect(Collectors.toList());
  }

  private boolean contains(List<MatchInput> matchInputs, String match) {
    return matchInputs.stream().anyMatch(m -> m.getMatch().equals(match));
  }

  protected abstract Builder createBatchResponseBuilder();

  protected abstract Builder createInputBuilder();

  protected abstract Builder getDefaultFeatureInput();

}
