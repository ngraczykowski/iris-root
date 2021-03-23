package com.silenteight.adjudication.engine.dataset;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)

@Sql
public class DatasetAlertRepositoryIT extends BaseDataJpaTest {

  @Autowired
  private DatasetAlertRepository datasetAlertRepository;
  @Autowired
  private DatasetRepository datasetRepository;

  @Test
  void shouldPersistDatasetAlert() {
    var dataset = DatasetEntity.builder()
        .build();
    datasetRepository.save(dataset);
    var datasetAlert = DatasetFixture.newDatasetAlertEntity(DatasetAlertKey.builder()
        .datasetId(dataset.getId())
        .alertId(1L)
        .build());
    var savedDatasetAlert = datasetAlertRepository.save(datasetAlert);
    entityManager.flush();

    assertThat(savedDatasetAlert)
        .usingRecursiveComparison()
        .ignoringFields("datasetId")
        .isEqualTo(datasetAlert);
  }

  @Test
  void shouldCountAlertsByDatasetId() {
    var dataset = DatasetEntity.builder()
        .build();
    datasetRepository.save(dataset);
    for (long i = 1; i <= 10; i++) {
      var datasetAlert = DatasetFixture.newDatasetAlertEntity(DatasetAlertKey.builder()
          .datasetId(dataset.getId())
          .alertId(i)
          .build());
      datasetAlertRepository.save(datasetAlert);
    }
    entityManager.flush();
    var countByDatasetId = datasetAlertRepository.countByIdDatasetId(dataset.getId());
    assertThat(countByDatasetId).isEqualTo(10);
  }

  @Test
  void shouldFindAlertsByDatasetIdUsePaging() {
    var dataset = DatasetEntity.builder()
        .build();
    datasetRepository.save(dataset);
    for (long i = 1; i <= 10; i++) {
      var datasetAlert = DatasetFixture.newDatasetAlertEntity(DatasetAlertKey.builder()
          .datasetId(dataset.getId())
          .alertId(i)
          .build());
      datasetAlertRepository.save(datasetAlert);
    }
    entityManager.flush();
    var page =
        datasetAlertRepository.findAllByIdDatasetId(
            PageRequest.of(0, 5),
            dataset.getId());
    assertThat(page.getTotalElements()).isEqualTo(10);
    assertThat(page.getSize()).isEqualTo(5);
    assertThat(page.hasNext()).isTrue();
  }
}
