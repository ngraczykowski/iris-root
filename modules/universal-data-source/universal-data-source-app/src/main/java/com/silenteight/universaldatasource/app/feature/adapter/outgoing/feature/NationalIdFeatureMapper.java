package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.nationalid.v1.BatchGetMatchNationalIdInputsResponse;
import com.silenteight.datasource.api.nationalid.v1.NationalIdFeatureInput;
import com.silenteight.datasource.api.nationalid.v1.NationalIdInput;
import com.silenteight.universaldatasource.app.feature.mapper.FeatureInputMapper;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class NationalIdFeatureMapper extends BaseFeatureMapper<NationalIdFeatureInput> {

  NationalIdFeatureMapper(final FeatureInputMapper featureInputMapper) {
    super(NationalIdFeatureInput.class, featureInputMapper);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetMatchNationalIdInputsResponse.newBuilder();
  }

  @Override
  protected Builder createInputBuilder() {
    return NationalIdInput.newBuilder();
  }

  @Override
  protected Builder getDefaultFeatureInput() {
    return NationalIdFeatureInput.newBuilder();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(NationalIdFeatureInput.class);
  }
}
