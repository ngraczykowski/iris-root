package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.compareDates.v1.BatchGetCompareDatesInputsResponse;
import com.silenteight.datasource.api.compareDates.v1.CompareDatesFeatureInput;
import com.silenteight.datasource.api.compareDates.v1.CompareDatesInput;
import com.silenteight.universaldatasource.app.feature.mapper.FeatureInputMapper;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class CompareDatesFeatureMapper extends BaseFeatureMapper<CompareDatesFeatureInput> {

  CompareDatesFeatureMapper(final FeatureInputMapper featureInputMapper) {
    super(CompareDatesFeatureInput.class, featureInputMapper);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetCompareDatesInputsResponse.newBuilder();
  }

  @Override
  protected Builder createInputBuilder() {
    return CompareDatesInput.newBuilder();
  }

  @Override
  protected Builder getDefaultFeatureInput() {
    return CompareDatesFeatureInput.newBuilder();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(CompareDatesFeatureInput.class);
  }
}
