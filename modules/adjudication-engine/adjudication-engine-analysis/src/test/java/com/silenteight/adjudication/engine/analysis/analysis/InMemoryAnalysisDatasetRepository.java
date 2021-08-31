package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class InMemoryAnalysisDatasetRepository implements AnalysisDatasetRepository {

  private final Map<AnalysisDatasetKey, TestAnalysisDatasetEntity> store = new HashMap<>();

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

          if (store.containsKey(id))
            testEntity.alertCount = store.get(id).alertCount;

          // Replace the test entity with new one.
          store.put(id, testEntity);
        })
        .collect(Collectors.toList());
  }

  public AnalysisDatasetQueryRepository getQueryRepository() {
    return new QueryRepository();
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
    public Stream<AnalysisDatasetQueryEntity> findAllByIdIn(Collection<AnalysisDatasetKey> ids) {
      return ids.stream()
          .filter(store::containsKey)
          .map(store::get)
          .map(TestAnalysisDatasetEntity::toQueryEntity);
    }
  }
}
