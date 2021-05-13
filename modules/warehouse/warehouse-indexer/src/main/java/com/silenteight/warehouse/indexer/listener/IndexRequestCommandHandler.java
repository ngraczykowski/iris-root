package com.silenteight.warehouse.indexer.listener;

import com.silenteight.data.api.v1.DataIndexRequest;
import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.warehouse.indexer.analysis.NamingStrategy;

public interface IndexRequestCommandHandler {

  DataIndexResponse handle(DataIndexRequest dataIndexRequest, NamingStrategy namingStrategy);
}
