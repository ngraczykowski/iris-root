package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.domain.AlertMatchIdComposite;
import com.silenteight.hsbc.bridge.model.ModelUseCase;
import com.silenteight.hsbc.bridge.model.SolvingModelDto;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class AdjudicationFacade {

  private final AlertService alertService;
  private final DatasetService datasetService;
  private final AnalysisService analysisService;

  public String createAnalysis(
      Collection<AlertMatchIdComposite> alertMatchIdComposites, ModelUseCase modelUseCase) {
    List<String> alertIds =
        alertService.createBatchAlertsWithMatches(alertMatchIdComposites);

    String datasetId = datasetService.createDataset(alertIds);
    SolvingModelDto solvingModel = modelUseCase.getSolvingModel();
    return analysisService.createAnalysisWithDataset(solvingModel, datasetId);
  }
}
