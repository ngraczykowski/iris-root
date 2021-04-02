package com.silenteight.warehouse.indexer.listener;

import com.silenteight.data.api.v1.DataIndexRequest;

public interface IndexRequestCommandHandler {

  void handle(DataIndexRequest dataIndexRequest);
}
