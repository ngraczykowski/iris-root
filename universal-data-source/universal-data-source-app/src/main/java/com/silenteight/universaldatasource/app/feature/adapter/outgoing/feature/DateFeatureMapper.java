package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.date.v1.BatchGetMatchDateInputsResponse;
import com.silenteight.datasource.api.date.v1.DateFeatureInput;
import com.silenteight.datasource.api.date.v1.DateInput;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class DateFeatureMapper extends BaseFeatureMapper<DateFeatureInput> {

  DateFeatureMapper() {
    super(DateFeatureInput.class);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetMatchDateInputsResponse.newBuilder();
  }

  @Override
  protected Builder createInputBuilder() {
    return DateInput.newBuilder();
  }

  @Override
  protected Builder getDefaultFeatureInput() {
    return DateFeatureInput.newBuilder();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(DateFeatureInput.class);
  }
}
