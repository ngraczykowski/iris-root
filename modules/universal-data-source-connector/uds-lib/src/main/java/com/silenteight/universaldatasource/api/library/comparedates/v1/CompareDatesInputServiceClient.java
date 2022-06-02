package com.silenteight.universaldatasource.api.library.comparedates.v1;

import java.util.List;

public interface CompareDatesInputServiceClient {

  List<BatchGetCompareDatesInputsOut> getBatchGetCompareDatesInputs(
      BatchGetCompareDatesInputsIn request);
}
