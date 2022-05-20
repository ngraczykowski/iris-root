package com.silenteight.adjudication.engine.analysis.dataset;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.adjudication.engine.analysis.analysis.DataSetRepository;
import com.silenteight.adjudication.engine.common.resource.ResourceName;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class InMemoryAnalysisDatasetRepository implements AnalysisDatasetRepository {

  private final Map<AnalysisDatasetKey, TestAnalysisDatasetEntity> store = new HashMap<>();
  private final Map<Long, Dataset> datasets = new HashMap<>();

  public List<AnalysisDatasetEntity> getAnalysisDatasetList() {
    return store
        .values()
        .stream()
        .map(TestAnalysisDatasetEntity::getEntity)
        .collect(Collectors.toUnmodifiableList());
  }

  @Override
  public Collection<AnalysisDatasetEntity> saveAll(Iterable<AnalysisDatasetEntity> entities) {
    return StreamSupport.stream(entities.spliterator(), false)
        .peek(e -> {
          var testEntity = new TestAnalysisDatasetEntity(e);
          var id = e.getId();

          if (datasets.containsKey(id.getDatasetId()))
            testEntity.alertCount = datasets.get(id.getDatasetId()).getAlertCount();

          // Replace the test entity with new one.
          store.put(id, testEntity);
        })
        .collect(Collectors.toList());
  }

  public AnalysisDatasetQueryRepository getQueryRepository() {
    return new QueryRepository();
  }

  public DataSetRepository getDatasetRepository() {
    return new InMemoryDatasetRepository();
  }

  @RequiredArgsConstructor
  @Getter
  private static final class TestAnalysisDatasetEntity {

    private final AnalysisDatasetEntity entity;

    private long alertCount;

    private AnalysisDatasetQueryEntity toQueryEntity() {
      return new AnalysisDatasetQueryEntity(entity.getId(), alertCount);
    }
  }

  private class QueryRepository implements AnalysisDatasetQueryRepository {

    @Override
    public Stream<AnalysisDatasetQueryEntity> findAllByIdIn(
        Collection<AnalysisDatasetKey> ids) {
      return ids.stream()
          .filter(store::containsKey)
          .map(store::get)
          .map(TestAnalysisDatasetEntity::toQueryEntity);
    }
  }

  private class InMemoryDatasetRepository implements DataSetRepository {

    @Override
    public void save(Dataset dataset) {
      var id = ResourceName.create(dataset.getName()).getLong("datasets");
      datasets.put(id, dataset);
    }
  }
}
