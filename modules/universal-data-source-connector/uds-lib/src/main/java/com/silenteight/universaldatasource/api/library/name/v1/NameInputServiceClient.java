package com.silenteight.universaldatasource.api.library.name.v1;

import java.util.List;

public interface NameInputServiceClient {

  List<BatchGetMatchNameInputsOut> batchGetMatchNameInputs(BatchGetMatchNameInputsIn request);
}
