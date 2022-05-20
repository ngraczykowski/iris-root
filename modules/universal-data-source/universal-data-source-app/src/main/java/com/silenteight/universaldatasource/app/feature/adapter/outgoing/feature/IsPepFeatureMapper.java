package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.ispep.v2.BatchGetMatchIsPepInputsResponse;
import com.silenteight.datasource.api.ispep.v2.IsPepFeatureInput;
import com.silenteight.datasource.api.ispep.v2.IsPepInput;
import com.silenteight.universaldatasource.app.feature.mapper.FeatureInputMapper;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class IsPepFeatureMapper extends BaseFeatureMapper<IsPepFeatureInput> {

  IsPepFeatureMapper(final FeatureInputMapper featureInputMapper) {
    super(IsPepFeatureInput.class, featureInputMapper);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetMatchIsPepInputsResponse.newBuilder();
  }

  @Override
  protected Builder createInputBuilder() {
    return IsPepInput.newBuilder();
  }

  @Override
  protected Builder getDefaultFeatureInput() {
    return IsPepFeatureInput.newBuilder();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(IsPepFeatureInput.class);
  }
}
