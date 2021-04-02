package com.silenteight.warehouse.indexer.indextestclient.gateway;

import com.silenteight.data.api.v1.DataIndexRequest;

public interface IndexClientGateway {

  void indexRequest(DataIndexRequest dataIndexRequest);
}
