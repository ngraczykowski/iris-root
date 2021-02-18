package com.silenteight.hsbc.bridge.bulk.repository;

import com.silenteight.hsbc.bridge.bulk.BulkProcessingResult;
import com.silenteight.hsbc.bridge.bulk.BulkStatus;
import com.silenteight.hsbc.bridge.bulk.dto.GetStatusQueryResult;
import com.silenteight.hsbc.bridge.bulk.query.GetResultQuery;
import com.silenteight.hsbc.bridge.bulk.query.GetStatusQuery;

public interface BulkQueryRepository {
  GetStatusQueryResult getStatus(GetStatusQuery query);
  BulkProcessingResult getResult(GetResultQuery query);
}
