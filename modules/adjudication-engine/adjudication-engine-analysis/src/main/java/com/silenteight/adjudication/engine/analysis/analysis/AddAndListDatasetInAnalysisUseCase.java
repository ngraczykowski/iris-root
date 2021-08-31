package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AnalysisDataset;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
class AddAndListDatasetInAnalysisUseCase {

  private final AddDatasetAlertsToAnalysisUseCase addDatasetAlertsToAnalysisUseCase;
  private final AddDatasetToAnalysisUseCase addDatasetToAnalysisUseCase;
  private final ListAnalysisDatasetUseCase listAnalysisDatasetUseCase;

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "analysis" })
  List<AnalysisDataset> batchAddAndListDataset(String analysis, List<String> datasets) {
    datasets.forEach(
        dataset -> addDatasetAlertsToAnalysisUseCase.addDatasetAlerts(analysis, dataset));

    var saved = addDatasetToAnalysisUseCase.batchAddDataset(analysis, datasets);

    return listAnalysisDatasetUseCase.listAnalysisDatasets(saved);
  }
}
