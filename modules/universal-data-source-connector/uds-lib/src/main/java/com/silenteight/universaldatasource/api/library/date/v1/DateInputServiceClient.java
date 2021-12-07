package com.silenteight.universaldatasource.api.library.date.v1;

import java.util.List;

public interface DateInputServiceClient {

  List<BatchGetMatchDateInputsOut> getBatchGetMatchDateInputs(BatchGetMatchDateInputsIn request);
}
