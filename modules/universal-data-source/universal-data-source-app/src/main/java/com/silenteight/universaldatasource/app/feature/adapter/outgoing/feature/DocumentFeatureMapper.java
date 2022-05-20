package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.document.v1.BatchGetMatchDocumentInputsResponse;
import com.silenteight.datasource.api.document.v1.DocumentFeatureInput;
import com.silenteight.datasource.api.document.v1.DocumentInput;
import com.silenteight.universaldatasource.app.feature.mapper.FeatureInputMapper;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class DocumentFeatureMapper extends BaseFeatureMapper<DocumentFeatureInput> {

  DocumentFeatureMapper(final FeatureInputMapper featureInputMapper) {
    super(DocumentFeatureInput.class, featureInputMapper);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetMatchDocumentInputsResponse.newBuilder();
  }

  @Override
  protected Builder createInputBuilder() {
    return DocumentInput.newBuilder();
  }

  @Override
  protected Builder getDefaultFeatureInput() {
    return DocumentFeatureInput.newBuilder();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(DocumentFeatureInput.class);
  }
}
