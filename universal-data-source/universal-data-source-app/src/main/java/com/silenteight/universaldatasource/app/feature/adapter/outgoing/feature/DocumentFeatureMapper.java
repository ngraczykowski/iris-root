package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.date.v1.BatchGetMatchDateInputsResponse;
import com.silenteight.datasource.api.document.v1.DocumentFeatureInput;
import com.silenteight.datasource.api.document.v1.DocumentInput;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class DocumentFeatureMapper extends BaseFeatureMapper<DocumentFeatureInput> {

  DocumentFeatureMapper() {
    super(DocumentFeatureInput.class);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetMatchDateInputsResponse.newBuilder();
  }

  @Override
  protected Builder createInputBuilder() {
    return DocumentInput.newBuilder();
  }

  @Override
  protected DocumentFeatureInput getDefaultFeatureInput() {
    return DocumentFeatureInput.getDefaultInstance();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(DocumentFeatureInput.class);
  }
}
