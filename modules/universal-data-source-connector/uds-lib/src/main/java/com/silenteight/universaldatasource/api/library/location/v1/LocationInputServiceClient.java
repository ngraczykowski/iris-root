package com.silenteight.universaldatasource.api.library.location.v1;

import java.util.List;

public interface LocationInputServiceClient {

  List<BatchGetMatchLocationInputsOut> batchGetMatchLocationInputs(
      BatchGetMatchLocationInputsIn request);
}
