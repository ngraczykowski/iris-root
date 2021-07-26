package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AnalysisDataset;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
class AddAndListDatasetsInAnalysisUseCase {

  @NonNull
  private final AddDatasetsToAnalysisUseCase addDatasetsToAnalysisUseCase;

  @NonNull
  private final ListAnalysisDatasetUseCase listAnalysisDatasetUseCase;

  @Timed("ae.analysis.use_case.analysis.add_datasets")
  List<AnalysisDataset> addAndListDatasets(String analysis, List<String> datasets) {
    var saved = addDatasetsToAnalysisUseCase.addDatasets(analysis, datasets);

    return listAnalysisDatasetUseCase.listAnalysisDatasets(saved
        .stream()
        .map(AnalysisDatasetEntity::getId)
        .collect(Collectors.toList()));
  }
}
