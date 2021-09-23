package com.silenteight.payments.bridge.ae.alertregistration.port;

import java.util.Optional;

public interface AnalysisDataAccessPort {

  Optional<Long> findTodayAnalysis();

  void save(long analysis);
}
