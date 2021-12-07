package com.silenteight.universaldatasource.api.library.allowlist.v1;

import java.util.List;

public interface AllowListInputServiceClient {

  List<BatchGetMatchAllowListInputsOut> getBatchGetMatchAllowListInputs(
      BatchGetMatchAllowListInputsIn request);
}
