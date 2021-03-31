package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AnalysisDataset;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
class AddDatasetsToAnalysisUseCase {

  @NonNull
  private final AnalysisDatasetRepository repository;

  @NonNull
  private final ListAnalysisDatasetUseCase listAnalysisDatasetUseCase;

  @Transactional
  List<AnalysisDataset> addDatasets(String analysis, List<String> datasets) {
    long analysisId = ResourceName.getResource(analysis).getId("analysis");

    List<AnalysisDatasetEntity> saved = datasets
        .stream()
        .map(datasetResource -> ResourceName.getResource(datasetResource).getId("datasets"))
        .map(datasetId -> AnalysisDatasetEntity
            .builder()
            .id(AnalysisDatasetKey
                .builder()
                .analysisId(analysisId)
                .datasetId(datasetId)
                .build())
            .build())
        .map(repository::save)
        .collect(Collectors.toList());

    return listAnalysisDatasetUseCase.listAnalysisDatasets(
        saved
            .stream()
            .map(AnalysisDatasetEntity::getId)
            .collect(Collectors.toList()));
  }
}
