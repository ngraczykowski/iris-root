package com.silenteight.warehouse.indexer.gateway;

import com.silenteight.data.api.v1.DataIndexResponse;

public interface IndexedConfirmationGateway {

  void alertIndexed(DataIndexResponse dataIndexResponse);
}
