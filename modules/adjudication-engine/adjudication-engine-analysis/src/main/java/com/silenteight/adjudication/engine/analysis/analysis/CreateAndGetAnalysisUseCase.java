package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.sep.base.aspects.metrics.Timed;

import com.google.protobuf.TextFormat;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
class CreateAndGetAnalysisUseCase {

  @NonNull private final CreateAnalysisUseCase createAnalysisUseCase;

  @NonNull private final GetAnalysisUseCase getAnalysisUseCase;

  @Timed(
      value = "ae.analysis.use_cases",
      extraTags = { "package", "analysis" },
      histogram = true,
      percentiles = { 0.5, 0.95, 0.99 }
  )
  Analysis createAndGetAnalysis(Analysis prototype) {
    var analysisName = createAnalysisUseCase.createAnalysis(prototype);
    var analysis = getAnalysisUseCase.getAnalysis(analysisName);

    log.info("Created new analysis: analysis={}", analysisName);
    if (log.isDebugEnabled()) {
      log.debug(
          "New analysis: analysisMessage={}", TextFormat.printer().shortDebugString(analysis));
    }

    return analysis;
  }
}
