package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.gender.v1.BatchGetMatchGenderInputsResponse;
import com.silenteight.datasource.api.gender.v1.GenderFeatureInput;
import com.silenteight.datasource.api.gender.v1.GenderInput;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class GenderFeatureMapper extends BaseFeatureMapper<GenderFeatureInput> {

  GenderFeatureMapper() {
    super(GenderFeatureInput.class);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetMatchGenderInputsResponse.newBuilder();
  }

  @Override
  protected Builder createInputBuilder() {
    return GenderInput.newBuilder();
  }

  @Override
  protected GenderFeatureInput getDefaultFeatureInput() {
    return GenderFeatureInput.getDefaultInstance();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(GenderFeatureInput.class);
  }
}
