package com.silenteight.adjudication.engine.analysis.dataset;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AnalysisDataset;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AnalysisDatasetFacade {

  private final AddDatasetAlertsToAnalysisUseCase addDatasetAlertsToAnalysisUseCase;
  private final AddDatasetToAnalysisUseCase addDatasetToAnalysisUseCase;
  private final ListAnalysisDatasetUseCase listAnalysisDatasetUseCase;

  @Timed(
      value = "ae.analysis.use_cases",
      extraTags = { "package", "analysis" },
      histogram = true,
      percentiles = { 0.5, 0.95, 0.99 }
  )
  public List<AnalysisDataset> batchAddAndListDataset(String analysis, List<String> datasets) {
    datasets.forEach(
        dataset -> addDatasetAlertsToAnalysisUseCase.addDatasetAlerts(analysis, dataset));

    var saved = addDatasetToAnalysisUseCase.batchAddDataset(analysis, datasets);

    return listAnalysisDatasetUseCase.listAnalysisDatasets(saved);
  }
}
