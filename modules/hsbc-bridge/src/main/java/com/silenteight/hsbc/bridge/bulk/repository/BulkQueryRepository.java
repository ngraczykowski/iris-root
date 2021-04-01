package com.silenteight.hsbc.bridge.bulk.repository;

import com.silenteight.hsbc.bridge.bulk.Bulk;
import com.silenteight.hsbc.bridge.bulk.BulkStatus;

import java.util.Collection;
import java.util.UUID;

public interface BulkQueryRepository {

  Bulk findById(UUID id);

  Boolean existsByStatusIn(Collection<BulkStatus> bulkStatuses);
}
