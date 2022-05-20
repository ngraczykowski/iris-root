package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.event.v1.BatchGetMatchEventInputsResponse;
import com.silenteight.datasource.api.event.v1.EventFeatureInput;
import com.silenteight.datasource.api.event.v1.EventInput;
import com.silenteight.universaldatasource.app.feature.mapper.FeatureInputMapper;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class EventFeatureMapper extends BaseFeatureMapper<EventFeatureInput> {

  EventFeatureMapper(final FeatureInputMapper featureInputMapper) {
    super(EventFeatureInput.class, featureInputMapper);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetMatchEventInputsResponse.newBuilder();
  }

  @Override
  protected Builder createInputBuilder() {
    return EventInput.newBuilder();
  }

  @Override
  protected Builder getDefaultFeatureInput() {
    return EventFeatureInput.newBuilder();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(EventFeatureInput.class);
  }
}
