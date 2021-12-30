package com.silenteight.universaldatasource.app.feature.adapter.outgoing.feature;

import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesFeatureInput;
import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesInput;
import com.silenteight.datasource.api.bankidentificationcodes.v1.BatchGetMatchBankIdentificationCodesInputsResponse;
import com.silenteight.universaldatasource.app.feature.port.outgoing.BaseFeatureMapper;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import org.springframework.stereotype.Component;

@Component
class BankIdentificationCodesFeatureMapper
    extends BaseFeatureMapper<BankIdentificationCodesFeatureInput> {

  BankIdentificationCodesFeatureMapper() {
    super(BankIdentificationCodesFeatureInput.class);
  }

  @Override
  protected Builder createBatchResponseBuilder() {
    return BatchGetMatchBankIdentificationCodesInputsResponse.newBuilder();
  }

  @Override
  protected Builder createInputBuilder() {
    return BankIdentificationCodesInput.newBuilder();
  }

  protected BankIdentificationCodesFeatureInput getDefaultFeatureInput() {
    return BankIdentificationCodesFeatureInput.getDefaultInstance();
  }

  @Override
  public Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException {
    return featureInput.unpack(BankIdentificationCodesFeatureInput.class);
  }
}
