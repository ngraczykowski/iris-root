package com.silenteight.universaldatasource.api.library.transaction.v1;

import java.util.List;

public interface TransactionInputServiceClient {

  List<BatchGetMatchTransactionInputsOut> batchGetMatchTransactionInputs(
      BatchGetMatchTransactionInputsIn request);
}
