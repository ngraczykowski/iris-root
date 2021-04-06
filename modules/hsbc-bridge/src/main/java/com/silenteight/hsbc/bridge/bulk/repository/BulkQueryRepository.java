package com.silenteight.hsbc.bridge.bulk.repository;

import com.silenteight.hsbc.bridge.bulk.Bulk;
import com.silenteight.hsbc.bridge.bulk.BulkStatus;

import java.util.Collection;

public interface BulkQueryRepository {

  Bulk findById(String id);

  Boolean existsByStatusIn(Collection<BulkStatus> bulkStatuses);
}
