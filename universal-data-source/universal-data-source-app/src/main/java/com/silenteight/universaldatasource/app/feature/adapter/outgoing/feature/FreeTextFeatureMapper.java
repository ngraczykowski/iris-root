package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.freetext.v1.BatchGetMatchFreeTextInputsResponse;
import com.silenteight.datasource.api.freetext.v1.FreeTextFeatureInput;
import com.silenteight.datasource.api.freetext.v1.FreeTextInput;
import com.silenteight.universaldatasource.app.feature.mapper.FeatureInputMapper;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class FreeTextFeatureMapper extends BaseFeatureMapper<FreeTextFeatureInput> {

  FreeTextFeatureMapper(final FeatureInputMapper featureInputMapper) {
    super(FreeTextFeatureInput.class, featureInputMapper);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetMatchFreeTextInputsResponse.newBuilder();
  }

  @Override
  protected Builder createInputBuilder() {
    return FreeTextInput.newBuilder();
  }

  @Override
  protected Builder getDefaultFeatureInput() {
    return FreeTextFeatureInput.newBuilder();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(FreeTextFeatureInput.class);
  }
}
