package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.historicaldecisions.v1.BatchGetMatchHistoricalDecisionsInputsResponse;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInput;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInput.HistoricalDecisionsFeatureInput;
import com.silenteight.universaldatasource.app.feature.mapper.FeatureInputMapper;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class HistoricalDecisionsFeatureMapper extends BaseFeatureMapper<HistoricalDecisionsFeatureInput> {

  HistoricalDecisionsFeatureMapper(final FeatureInputMapper featureInputMapper) {
    super(HistoricalDecisionsFeatureInput.class, featureInputMapper);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetMatchHistoricalDecisionsInputsResponse.newBuilder();
  }

  @Override
  protected Builder createInputBuilder() {
    return HistoricalDecisionsInput.newBuilder();
  }

  @Override
  protected Builder getDefaultFeatureInput() {
    return HistoricalDecisionsFeatureInput.newBuilder();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(HistoricalDecisionsFeatureInput.class);
  }
}
