package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.ispep.v1.BatchGetMatchIsPepSolutionsResponse;
import com.silenteight.datasource.api.ispep.v1.BatchGetMatchIsPepSolutionsResponse.Feature;
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
class IsPepFeatureMapperV1 implements FeatureMapper {


  @Override
  public String getType() {
    return Feature.class.getCanonicalName();
  }

  @Override
  public BatchFeatureInputResponse map(MatchFeatureOutput matchFeatureOutput) {

    var batchResponseBuilder = BatchGetMatchIsPepSolutionsResponse.newBuilder();

    // NOTE(jgajewski): IsPepAgent had chuckSize equal to 1.
    // That means that it has only one MatchInput in MatchFeatureOutput.
    for (MatchInput matchInput : matchFeatureOutput.getMatchInputs()) {

      batchResponseBuilder
          .setMatch(matchInput.getMatch());

      for (AgentInput agentInput : matchInput.getAgentInputs()) {
        var featureInputBuilder = Feature.newBuilder();
        JsonToStructConverter.map(featureInputBuilder, agentInput.getAgentInputJson());
        batchResponseBuilder.addFeatures(featureInputBuilder.build());
      }
    }

    return new BatchFeatureInputResponse(batchResponseBuilder.build());
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(Feature.class);
  }
}
