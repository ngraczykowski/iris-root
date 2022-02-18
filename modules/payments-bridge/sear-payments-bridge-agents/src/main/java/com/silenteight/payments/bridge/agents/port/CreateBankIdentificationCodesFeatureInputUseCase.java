package com.silenteight.payments.bridge.agents.port;

import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesFeatureInput;
import com.silenteight.payments.bridge.agents.model.BankIdentificationCodesAgentsRequest;

public interface CreateBankIdentificationCodesFeatureInputUseCase {

  BankIdentificationCodesFeatureInput create(BankIdentificationCodesAgentsRequest request);
}
