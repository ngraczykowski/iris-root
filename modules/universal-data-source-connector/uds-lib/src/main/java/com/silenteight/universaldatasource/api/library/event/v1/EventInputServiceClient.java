package com.silenteight.universaldatasource.api.library.event.v1;

import java.util.List;

public interface EventInputServiceClient {

  List<BatchGetMatchEventInputsOut> getBatchGetMatchEventInputs(BatchGetMatchEventInputsIn request);
}
