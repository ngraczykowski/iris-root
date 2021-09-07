package com.silenteight.payments.bridge.datasource.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsResponse;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.payments.bridge.datasource.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class NameFeatureMapper extends BaseFeatureMapper<NameFeatureInput> {

  NameFeatureMapper() {
    super(NameFeatureInput.class);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetMatchNameInputsResponse.newBuilder();
  }
}
