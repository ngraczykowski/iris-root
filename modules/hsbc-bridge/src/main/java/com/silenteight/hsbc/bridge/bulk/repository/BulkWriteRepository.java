package com.silenteight.hsbc.bridge.bulk.repository;

import com.silenteight.hsbc.bridge.bulk.BulkStatus;
import com.silenteight.hsbc.bridge.bulk.dto.CreateBulkResult;
import com.silenteight.hsbc.bridge.bulk.dto.UpdateBulkResult;
import com.silenteight.hsbc.bridge.rest.model.input.Alerts;

import java.util.UUID;

public interface BulkWriteRepository {

  CreateBulkResult createBulk(Alerts alerts);
  UpdateBulkResult updateBulkStatus(UUID id, BulkStatus status);
}
