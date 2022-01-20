package com.silenteight.warehouse.indexer.production.qa;

import com.silenteight.data.api.v2.QaDataIndexRequest;

public interface QaIndexRequestCommandHandler {

  void handle(QaDataIndexRequest request);
}
