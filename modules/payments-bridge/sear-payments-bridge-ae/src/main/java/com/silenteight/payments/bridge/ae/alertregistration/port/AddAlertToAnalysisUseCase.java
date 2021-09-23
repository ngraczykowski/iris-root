package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;

import java.util.List;

public interface AddAlertToAnalysisUseCase {

  void addAlertToAnalysis(List<RegisterAlertRequest> registerAlertRequest);
}
