package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.BatchAddAlertsRequest;
import com.silenteight.adjudication.api.v1.BatchAddAlertsResponse;
import com.silenteight.adjudication.api.v1.CreateAnalysisRequest;

public interface AnalysisClientPort {

  Analysis createAnalysis(CreateAnalysisRequest request);

  BatchAddAlertsResponse addAlertToAnalysis(BatchAddAlertsRequest request);
}
