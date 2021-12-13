package com.silenteight.payments.bridge.firco.metrics.efficiency;

public interface EfficiencyMetricIncrementerPort {

  void incrementManualInvestigation();

  void incrementRecommendedAction(String recommendedAction);

}
