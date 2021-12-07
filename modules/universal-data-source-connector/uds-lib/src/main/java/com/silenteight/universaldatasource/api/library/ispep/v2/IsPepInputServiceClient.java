package com.silenteight.universaldatasource.api.library.ispep.v2;

import java.util.List;

public interface IsPepInputServiceClient {

  List<BatchGetMatchIsPepInputsOut> batchGetMatchIsPepInputs(BatchGetMatchIsPepInputsIn request);
}
