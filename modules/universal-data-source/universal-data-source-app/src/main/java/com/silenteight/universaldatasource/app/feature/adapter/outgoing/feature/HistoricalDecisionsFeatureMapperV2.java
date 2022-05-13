package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.historicaldecisions.v2.BatchGetMatchHistoricalDecisionsInputsResponse;
import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsFeatureInput;
import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsInput;
import com.silenteight.universaldatasource.app.feature.mapper.FeatureInputMapper;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class HistoricalDecisionsFeatureMapperV2
    extends BaseFeatureMapper<HistoricalDecisionsFeatureInput> {

  HistoricalDecisionsFeatureMapperV2(final FeatureInputMapper featureInputMapper) {
    super(HistoricalDecisionsFeatureInput.class, featureInputMapper);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetMatchHistoricalDecisionsInputsResponse.newBuilder();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(HistoricalDecisionsFeatureInput.class);
  }

  @Override
  protected Builder createInputBuilder() {
    return HistoricalDecisionsInput.newBuilder();
  }

  @Override
  protected Builder getDefaultFeatureInput() {
    return HistoricalDecisionsFeatureInput.newBuilder();
  }
}
