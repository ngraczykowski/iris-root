package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.AddAlertToAnalysisCommand;
import com.silenteight.bridge.core.registration.domain.model.Analysis;
import com.silenteight.bridge.core.registration.domain.model.DefaultModel;

import com.google.protobuf.Timestamp;

import java.util.List;

public interface AnalysisService {

  Analysis create(DefaultModel defaultModel);

  void addAlertsToAnalysis(
      String analysisName, List<AddAlertToAnalysisCommand> alerts, Timestamp alertDeadlineTime);
}
