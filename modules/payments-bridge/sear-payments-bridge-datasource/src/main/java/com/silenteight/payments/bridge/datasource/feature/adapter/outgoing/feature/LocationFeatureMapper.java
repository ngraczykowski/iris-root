package com.silenteight.payments.bridge.datasource.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsResponse;
import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.payments.bridge.datasource.feature.port.outgoing.BaseFeatureMapper;

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
}
