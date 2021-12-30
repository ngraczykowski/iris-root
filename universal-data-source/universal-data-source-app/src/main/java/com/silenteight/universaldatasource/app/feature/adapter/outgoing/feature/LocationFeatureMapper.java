package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsResponse;
import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.datasource.api.location.v1.LocationInput;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class LocationFeatureMapper extends BaseFeatureMapper<LocationFeatureInput> {

  LocationFeatureMapper() {
    super(LocationFeatureInput.class);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetMatchLocationInputsResponse.newBuilder();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(LocationFeatureInput.class);
  }

  @Override
  protected Builder createInputBuilder() {
    return LocationInput.newBuilder();
  }

  protected LocationFeatureInput getDefaultFeatureInput() {
    return LocationFeatureInput.getDefaultInstance();
  }
}
