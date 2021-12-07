package com.silenteight.universaldatasource.api.library.nationalid.v1;

import java.util.List;

public interface NationalIdInputServiceClient {

  List<BatchGetMatchNationalIdInputsOut> batchGetMatchTransactionInputs(
      BatchGetMatchNationalIdInputsIn request);
}
