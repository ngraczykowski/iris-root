package com.silenteight.warehouse.qa.handler;

import com.silenteight.data.api.v2.QaDataIndexRequest;

public interface QaRequestCommandHandler {

  void handle(QaDataIndexRequest request);
}
