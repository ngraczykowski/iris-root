package com.silenteight.universaldatasource.api.library.gender.v1;

import java.util.List;

public interface GenderInputServiceClient {

  List<BatchGetMatchGenderInputsOut> getBatchGetMatchGenderInputs(
      BatchGetMatchGenderInputsIn request);
}
