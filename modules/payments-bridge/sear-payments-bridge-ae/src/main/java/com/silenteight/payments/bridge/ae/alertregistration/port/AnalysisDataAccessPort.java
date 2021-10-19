package com.silenteight.payments.bridge.ae.alertregistration.port;

import java.util.Optional;

public interface AnalysisDataAccessPort {

  Optional<String> findCurrentAnalysis();

  Optional<String> save(String analysis);
}
