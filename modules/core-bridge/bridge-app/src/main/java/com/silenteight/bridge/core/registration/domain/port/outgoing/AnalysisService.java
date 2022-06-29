package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.AlertsAddedToAnalysis;
import com.silenteight.bridge.core.registration.domain.model.Analysis;
import com.silenteight.bridge.core.registration.domain.model.DefaultModel;

import com.google.protobuf.Timestamp;

import java.util.List;

public interface AnalysisService {

  Analysis create(DefaultModel defaultModel);

  AlertsAddedToAnalysis addAlertsToAnalysis(
      String analysisName, List<String> alertNames, Timestamp alertDeadlineTime);
}
