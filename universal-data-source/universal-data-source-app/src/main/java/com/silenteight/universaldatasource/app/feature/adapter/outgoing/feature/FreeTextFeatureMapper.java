package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.freetext.v1.BatchGetMatchFreeTextInputsResponse;
import com.silenteight.datasource.api.freetext.v1.FreeTextFeatureInput;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class FreeTextFeatureMapper extends BaseFeatureMapper<FreeTextFeatureInput> {

  FreeTextFeatureMapper() {
    super(FreeTextFeatureInput.class);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetMatchFreeTextInputsResponse.newBuilder();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(FreeTextFeatureInput.class);
  }
}
