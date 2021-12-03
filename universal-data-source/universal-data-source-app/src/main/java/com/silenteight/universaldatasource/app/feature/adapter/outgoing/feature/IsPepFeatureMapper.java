package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.ispep.v2.BatchGetMatchIsPepInputsResponse;
import com.silenteight.datasource.api.ispep.v2.IsPepFeatureInput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput.AgentInput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput.MatchInput;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchFeatureInputResponse;
import com.silenteight.universaldatasource.app.feature.port.outgoing.FeatureMapper;
import com.silenteight.universaldatasource.common.protobuf.JsonToStructConverter;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import org.springframework.stereotype.Component;

@Component
class IsPepFeatureMapper implements FeatureMapper {

  @Override
  public String getType() {
    return IsPepFeatureInput.class.getCanonicalName();
  }

  @Override
  public BatchFeatureInputResponse map(MatchFeatureOutput matchFeatureOutput) {
    var batchResponseBuilder = BatchGetMatchIsPepInputsResponse.newBuilder();
    var nameInputField = batchResponseBuilder.getDescriptorForType().findFieldByNumber(1);

    for (MatchInput matchInput : matchFeatureOutput.getMatchInputs()) {

      var inputBuilder = batchResponseBuilder.newBuilderForField(nameInputField);

      var matchField = inputBuilder.getDescriptorForType().findFieldByNumber(1);
      inputBuilder.setField(matchField, matchInput.getMatch());

      var featureInputsField = inputBuilder.getDescriptorForType().findFieldByNumber(2);

      for (AgentInput agentInput : matchInput.getAgentInputs()) {
        var featureInputBuilder = inputBuilder.newBuilderForField(featureInputsField);
        JsonToStructConverter.map(featureInputBuilder, agentInput.getAgentInputJson());
        //NOTE(jgajewski): Only difference from other agents is that isPep doesn't have repeated
        //input field (IsPepFeatureInput)
        inputBuilder.setField(featureInputsField, featureInputBuilder.build());
      }

      batchResponseBuilder.addRepeatedField(nameInputField, inputBuilder.build());
    }

    return new BatchFeatureInputResponse(batchResponseBuilder.build());
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(IsPepFeatureInput.class);
  }
}
