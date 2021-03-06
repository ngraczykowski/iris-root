package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.country.v1.BatchGetMatchCountryInputsResponse;
import com.silenteight.datasource.api.country.v1.CountryFeatureInput;
import com.silenteight.datasource.api.country.v1.CountryInput;
import com.silenteight.universaldatasource.app.feature.mapper.FeatureInputMapper;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class CountryFeatureMapper extends BaseFeatureMapper<CountryFeatureInput> {

  CountryFeatureMapper(final FeatureInputMapper featureInputMapper) {
    super(CountryFeatureInput.class, featureInputMapper);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetMatchCountryInputsResponse.newBuilder();
  }

  @Override
  protected Builder createInputBuilder() {
    return CountryInput.newBuilder();
  }

  protected Builder getDefaultFeatureInput() {
    return CountryFeatureInput.newBuilder();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(CountryFeatureInput.class);
  }
}
