package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.analysis.event.DatasetsAddedToAnalysisEvent;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

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
    long analysisId = ResourceName.getResource(analysis).getId("analysis");

    long[] datasetIds = new long[datasets.size()];

    var datasetEntities = IntStream.range(0, datasets.size())
        .mapToObj(idx -> {
          var datasetId = ResourceName.getResource(datasets.get(idx)).getId("datasets");
          datasetIds[idx] = datasetId;
          var entity = createEntity(analysisId, datasetId);
          return repository.save(entity);
        })
        .collect(toList());

    eventPublisher.publishEvent(new DatasetsAddedToAnalysisEvent(analysisId, datasetIds));

    return datasetEntities;
  }

  private static AnalysisDatasetEntity createEntity(long analysisId, long datasetId) {
    return AnalysisDatasetEntity
        .builder()
        .id(AnalysisDatasetKey
            .builder()
            .analysisId(analysisId)
            .datasetId(datasetId)
            .build())
        .build();
  }
}
