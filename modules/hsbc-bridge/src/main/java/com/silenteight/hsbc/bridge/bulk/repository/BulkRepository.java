package com.silenteight.hsbc.bridge.bulk.repository;

import com.silenteight.hsbc.bridge.bulk.Bulk;

import org.springframework.data.repository.Repository;

import java.util.UUID;

public interface BulkRepository
    extends Repository<Bulk, UUID>, BulkWriteRepository, BulkQueryRepository {

}
