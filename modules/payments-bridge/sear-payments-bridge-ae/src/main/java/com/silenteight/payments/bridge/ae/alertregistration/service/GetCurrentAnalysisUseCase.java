package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisDataAccessPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.BuildCreateAnalysisRequestPort;
import com.silenteight.payments.bridge.governance.solvingmodel.port.CheckDefaultModelExistsUseCase;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class GetCurrentAnalysisUseCase {

  private final AnalysisDataAccessPort analysisDataAccessPort;
  private final CreateAnalysisService createAnalysisService;
  private final BuildCreateAnalysisRequestPort buildCreateAnalysisRequestPort;
  private final CheckDefaultModelExistsUseCase checkDefaultModelExistsUseCase;

  Optional<String> getOrCreateAnalysis() {
    var current = analysisDataAccessPort
        .findCurrentAnalysis();

    if (current.isPresent()) {
      return current;
    }

    return createNewAnalysis();
  }

  private Optional<String> createNewAnalysis() {
    if (!checkDefaultModelExistsUseCase.checkDefaultModelExists()) {
      return Optional.empty();
    }
    return Optional.of(createAnalysisService.createAnalysis(
        buildCreateAnalysisRequestPort.buildFromCurrentModel()));
  }
}
