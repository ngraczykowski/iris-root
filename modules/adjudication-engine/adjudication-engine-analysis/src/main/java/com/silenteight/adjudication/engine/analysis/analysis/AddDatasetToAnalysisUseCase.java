package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("FeatureEnvy")
@RequiredArgsConstructor
@Service
@Slf4j
class AddDatasetToAnalysisUseCase {

  private final AnalysisDatasetRepository repository;

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "analysis" })
  @Transactional
  List<AnalysisDatasetKey> batchAddDataset(String analysis, List<String> datasets) {
    long analysisId = ResourceName.create(analysis).getLong("analysis");

    // FIXME(ahaczewski): Make this operation idempotent, as it might fail when publishing
    //  message to RabbitMQ, but after commit has succeeded.
    return repository
        .saveAll(() -> datasets.stream().map(dataset -> {
          var datasetId = ResourceName.create(dataset).getLong("datasets");
          return new AnalysisDatasetEntity(analysisId, datasetId);
        }).iterator())
        .stream()
        .map(AnalysisDatasetEntity::getId)
        .collect(toList());
  }
}
