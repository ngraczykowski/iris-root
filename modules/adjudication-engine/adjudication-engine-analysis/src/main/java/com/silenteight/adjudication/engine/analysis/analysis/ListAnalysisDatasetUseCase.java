package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AnalysisDataset;
import com.silenteight.sep.base.common.exception.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
class ListAnalysisDatasetUseCase {

  @NonNull
  private final AnalysisDatasetQueryRepository analysisDatasetQueryRepository;

  @Transactional(readOnly = true)
  List<AnalysisDataset> listAnalysisDatasets(List<AnalysisDatasetKey> ids) {
    List<AnalysisDataset> result = new ArrayList<>();

    for (AnalysisDatasetKey id : ids) {
      AnalysisDatasetQueryEntity analysisDatasetQueryEntity = analysisDatasetQueryRepository
          .findById(id)
          .orElseThrow(() -> new EntityNotFoundException(id));
      result.add(analysisDatasetQueryEntity.toAnalysisDataset());
    }

    return result;
  }
}
