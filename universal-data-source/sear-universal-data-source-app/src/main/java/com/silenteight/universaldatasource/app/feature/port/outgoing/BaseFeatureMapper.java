package com.silenteight.universaldatasource.app.feature.port.outgoing;


import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput.AgentInput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput.MatchInput;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchFeatureInputResponse;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import com.google.protobuf.util.JsonFormat;

public abstract class BaseFeatureMapper<T extends Message> implements FeatureMapper {

  private final String className;

  protected BaseFeatureMapper(Class<T> featureInputType) {
    this.className = featureInputType.getSimpleName();
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
        mapToMessage(featureInputBuilder, agentInput);
        inputBuilder.addRepeatedField(featureInputsField, featureInputBuilder.build());
      }

      batchResponseBuilder.addRepeatedField(nameInputField, inputBuilder.build());
    }

    return new BatchFeatureInputResponse(batchResponseBuilder.build());
  }

  private static void mapToMessage(Builder featureInputBuilder, AgentInput agentInput) {
    try {
      JsonFormat.parser().merge(
          agentInput.getAgentInputJson(),
          featureInputBuilder);
    } catch (InvalidProtocolBufferException e) {
      throw new JsonStructConversionException("Cannot convert agent feature input to Struct", e);
    }
  }

  protected abstract Builder createBatchResponseBuilder();

  private static class JsonStructConversionException extends RuntimeException {

    private static final long serialVersionUID = -204330995574517285L;

    JsonStructConversionException(String message, Throwable cause) {
      super(message, cause);
    }
  }

}
