package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.isofgivendocumenttype.v1.BatchGetIsOfGivenDocumentTypeInputsResponse;
import com.silenteight.datasource.api.isofgivendocumenttype.v1.IsOfGivenDocumentTypeFeatureInput;
import com.silenteight.datasource.api.isofgivendocumenttype.v1.IsOfGivenDocumentTypeInput;
import com.silenteight.universaldatasource.app.feature.mapper.FeatureInputMapper;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class IsOfGivenDocumentTypeMapper extends BaseFeatureMapper<IsOfGivenDocumentTypeFeatureInput> {

  IsOfGivenDocumentTypeMapper(final FeatureInputMapper featureInputMapper) {
    super(IsOfGivenDocumentTypeFeatureInput.class, featureInputMapper);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetIsOfGivenDocumentTypeInputsResponse.newBuilder();
  }

  @Override
  protected Builder createInputBuilder() {
    return IsOfGivenDocumentTypeInput.newBuilder();
  }

  @Override
  protected Builder getDefaultFeatureInput() {
    return IsOfGivenDocumentTypeFeatureInput.newBuilder();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(IsOfGivenDocumentTypeFeatureInput.class);
  }
}
