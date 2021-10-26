package com.silenteight.warehouse.backup.indexing;

import com.silenteight.data.api.v2.ProductionDataIndexRequest;

public interface ProductionDataIndexStorable {

  void save(ProductionDataIndexRequest request);
}
