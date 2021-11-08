package com.silenteight.universaldatasource.app.feature.port.outgoing;


import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput.AgentInput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput.MatchInput;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchFeatureInputResponse;
import com.silenteight.universaldatasource.common.protobuf.JsonToStructConverter;

import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;

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

    return new BatchFeatureInputResponse(batchResponseBuilder.build());
  }

  protected abstract Builder createBatchResponseBuilder();

}
