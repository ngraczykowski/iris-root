package com.silenteight.universaldatasource.api.library.historicaldecisions.v1;

import java.util.List;

public interface HistoricalDecisionsInputServiceClient {

  List<BatchGetMatchHistoricalDecisionsInputsOut> getBatchGetMatchHistoricalDecisionsInputs(
      BatchGetMatchHistoricalDecisionsInputsIn request);
}
