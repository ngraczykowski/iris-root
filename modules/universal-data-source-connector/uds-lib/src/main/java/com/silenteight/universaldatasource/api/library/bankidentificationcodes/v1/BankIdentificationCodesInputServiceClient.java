package com.silenteight.universaldatasource.api.library.bankidentificationcodes.v1;

import java.util.List;

public interface BankIdentificationCodesInputServiceClient {

  List<BatchGetMatchBankIdentificationCodesInputsOut> getBatchGetMatchBankIdentificationCodesInputs(
      BatchGetMatchBankIdentificationCodesInputsIn request);
}
