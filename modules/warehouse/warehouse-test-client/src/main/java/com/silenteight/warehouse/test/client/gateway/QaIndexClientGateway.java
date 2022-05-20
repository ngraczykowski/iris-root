package com.silenteight.warehouse.test.client.gateway;

import com.silenteight.data.api.v2.QaDataIndexRequest;

public interface QaIndexClientGateway {

  void indexRequest(QaDataIndexRequest dataIndexRequest);
}
