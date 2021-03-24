package com.silenteight.adjudication.engine.dataset.dataset;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
class DatasetRepositoryIT extends BaseDataJpaTest {

  @Autowired
  private DatasetRepository repository;

  @Test
  void shouldPersistDatasetAndFind() {
    var dataset = repository.save(DatasetFixture.randomDatasetEntity());
    var datasetFound = repository.findById(dataset.getId());

    assertThat(datasetFound.isPresent()).isTrue();
    assertThat(datasetFound.get())
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(dataset);
  }

  @Test
  void shouldFindAll() {
    for (int i = 0; i < 10; i++) {
      repository.save(DatasetFixture.randomDatasetEntity());
    }
    var page = repository.findAll(PageRequest.of(0, 3));

    assertThat(page.getTotalElements()).isEqualTo(10);
    assertThat(page.getSize()).isEqualTo(3);
  }
}
