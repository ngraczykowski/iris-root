package com.silenteight.universaldatasource.api.library.document.v1;

import java.util.List;

public interface DocumentInputServiceClient {

  List<BatchGetMatchDocumentInputsOut> getBatchGetMatchDocumentInputs(
      BatchGetMatchDocumentInputsIn request);
}
