package com.silenteight.universaldatasource.api.library.freetext.v1;

import java.util.List;

public interface FreeTextInputServiceClient {

  List<BatchGetMatchFreeTextInputsOut> getBatchGetMatchFreeTextInputs(
      BatchGetMatchFreeTextInputsIn request);
}
