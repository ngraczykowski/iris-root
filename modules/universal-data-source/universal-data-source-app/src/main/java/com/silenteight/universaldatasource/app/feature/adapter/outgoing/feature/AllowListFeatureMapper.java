package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.allowlist.v1.AllowListFeatureInput;
import com.silenteight.datasource.api.allowlist.v1.AllowListInput;
import com.silenteight.datasource.api.allowlist.v1.BatchGetMatchAllowListInputsResponse;
import com.silenteight.universaldatasource.app.feature.mapper.FeatureInputMapper;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class AllowListFeatureMapper extends BaseFeatureMapper<AllowListFeatureInput> {

  AllowListFeatureMapper(final FeatureInputMapper featureInputMapper) {
    super(AllowListFeatureInput.class, featureInputMapper);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetMatchAllowListInputsResponse.newBuilder();
  }


  @Override
  protected Builder createInputBuilder() {
    return AllowListInput.newBuilder();
  }

  protected Builder getDefaultFeatureInput() {
    return AllowListFeatureInput.newBuilder();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(AllowListFeatureInput.class);
  }
}
