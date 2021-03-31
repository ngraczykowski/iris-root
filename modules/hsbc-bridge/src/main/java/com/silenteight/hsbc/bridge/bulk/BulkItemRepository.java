package com.silenteight.hsbc.bridge.bulk;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

interface BulkItemRepository extends Repository<Bulk, UUID> {

  Optional<BulkItem> findById(long bulkItemId);

  void save(BulkItem bulkItem);
}
