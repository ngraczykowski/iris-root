package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.transaction.v1.BatchGetMatchTransactionInputsResponse;
import com.silenteight.datasource.api.transaction.v1.TransactionFeatureInput;
import com.silenteight.datasource.api.transaction.v1.TransactionInput;
import com.silenteight.universaldatasource.app.feature.mapper.FeatureInputMapper;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class TransactionFeatureMapper extends BaseFeatureMapper<TransactionFeatureInput> {

  TransactionFeatureMapper(final FeatureInputMapper featureInputMapper) {
    super(TransactionFeatureInput.class, featureInputMapper);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetMatchTransactionInputsResponse.newBuilder();
  }


  @Override
  protected Builder createInputBuilder() {
    return TransactionInput.newBuilder();
  }

  @Override
  protected Builder getDefaultFeatureInput() {
    return TransactionFeatureInput.newBuilder();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(TransactionFeatureInput.class);
  }
}
