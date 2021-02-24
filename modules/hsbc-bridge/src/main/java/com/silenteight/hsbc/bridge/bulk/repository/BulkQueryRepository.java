package com.silenteight.hsbc.bridge.bulk.repository;

import com.silenteight.hsbc.bridge.bulk.Bulk;

import java.util.UUID;

public interface BulkQueryRepository {
  Bulk findById(UUID id);
}
