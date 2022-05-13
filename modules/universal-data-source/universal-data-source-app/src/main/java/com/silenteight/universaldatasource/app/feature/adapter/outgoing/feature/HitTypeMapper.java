package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.hittype.v1.BatchGetMatchHitTypeInputsResponse;
import com.silenteight.datasource.api.hittype.v1.HitTypeFeatureInput;
import com.silenteight.datasource.api.hittype.v1.HitTypeInput;
import com.silenteight.universaldatasource.app.feature.mapper.FeatureInputMapper;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class HitTypeMapper extends BaseFeatureMapper<HitTypeFeatureInput> {

  HitTypeMapper(final FeatureInputMapper featureInputMapper) {
    super(HitTypeFeatureInput.class, featureInputMapper);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetMatchHitTypeInputsResponse.newBuilder();
  }

  @Override
  protected Builder createInputBuilder() {
    return HitTypeInput.newBuilder();
  }

  @Override
  protected Builder getDefaultFeatureInput() {
    return HitTypeFeatureInput.newBuilder();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(HitTypeFeatureInput.class);
  }
}
