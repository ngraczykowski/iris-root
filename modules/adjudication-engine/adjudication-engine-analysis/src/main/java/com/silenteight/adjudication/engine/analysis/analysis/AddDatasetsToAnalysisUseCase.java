package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.internal.v1.AddedAnalysisDatasets;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
class AddDatasetsToAnalysisUseCase {

  @NonNull
  private final AnalysisDatasetRepository repository;

  @NonNull
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  List<AnalysisDatasetEntity> addDatasets(String analysis, List<String> datasets) {
    long analysisId = ResourceName.create(analysis).getLong("analysis");

    var eventBuilder = AddedAnalysisDatasets.newBuilder();
    var datasetEntities = datasets.stream()
        .map(dataset -> {
          var datasetId = ResourceName.create(dataset).getLong("datasets");
          var entity = new AnalysisDatasetEntity(analysisId, datasetId);
          eventBuilder.addAnalysisDatasets(entity.getName());
          return repository.save(entity);
        })
        .collect(toList());

    eventPublisher.publishEvent(eventBuilder.build());

    return datasetEntities;
  }
}
