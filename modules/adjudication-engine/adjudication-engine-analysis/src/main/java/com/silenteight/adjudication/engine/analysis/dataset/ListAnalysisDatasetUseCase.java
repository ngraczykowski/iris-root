package com.silenteight.adjudication.engine.analysis.dataset;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AnalysisDataset;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
class ListAnalysisDatasetUseCase {

  @NonNull
  private final AnalysisDatasetQueryRepository analysisDatasetQueryRepository;

  @Transactional(readOnly = true)
  List<AnalysisDataset> listAnalysisDatasets(
      List<AnalysisDatasetKey> ids) {
    return analysisDatasetQueryRepository.findAllByIdIn(ids)
        .map(AnalysisDatasetQueryEntity::toAnalysisDataset)
        .collect(Collectors.toList());
  }
}
