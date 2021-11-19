package com.silenteight.warehouse.test.client.gateway;

import com.silenteight.dataretention.api.v1.AnalysisExpired;

public interface AnalysisExpiredClientGateway {

  void indexRequest(AnalysisExpired analysisExpired);
}
