package com.silenteight.warehouse.backup.indexing;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;

public interface ProductionDataIndexStorable {
  void save(ProductionDataIndexRequest request);
}
