package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisDataAccessPort;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GetCurrentAnalysisUseCase {

  private final AnalysisDataAccessPort analysisDataAccessPort;
  private final CreateAnalysisService createAnalysisService;

  // FIXME(ahaczewski): Stop using IDs! The Analysis name is a String, not Long!!!
  long getOrCreateAnalysis() {
    var analysis = analysisDataAccessPort.findCurrentAnalysis();

    if (analysis.isEmpty())
      return createAnalysisService.createAnalysis();

    return analysis.get();
  }
}
