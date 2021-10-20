package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.adjudication.api.v1.CreateAnalysisRequest;

public interface CreateAnalysisUseCase {

  String createAnalysis(CreateAnalysisRequest request);
}
