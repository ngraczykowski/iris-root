package com.silenteight.adjudication.engine.dataset.dataset;

import com.silenteight.adjudication.engine.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)

@Sql
public class DatasetAlertRepositoryIT extends BaseDataJpaTest {

  @Autowired
  private DatasetAlertRepository datasetAlertRepository;
  @Autowired
  private DatasetRepository datasetRepository;
  @Autowired
  JdbcTemplate jdbcTemplate;

  @Test
  void shouldPersistDatasetAlert() {
    DatasetEntity dataset = createDatasetEntity();
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
    DatasetEntity dataset = createDatasetEntity();
    createDatasetAlertsEntity(dataset.getId());
    entityManager.flush();
    var countByDatasetId = datasetAlertRepository.countByIdDatasetId(dataset.getId());
    assertThat(countByDatasetId).isEqualTo(10);
  }

  @Test
  void shouldFindAlertsByDatasetIdUsePaging() {
    DatasetEntity dataset = createDatasetEntity();
    createDatasetAlertsEntity(dataset.getId());

    entityManager.flush();
    var page =
        datasetAlertRepository.findAllByIdDatasetId(
            PageRequest.of(0, 5),
            dataset.getId());
    assertThat(page.getTotalElements()).isEqualTo(10);
    assertThat(page.getSize()).isEqualTo(5);
    assertThat(page.hasNext()).isTrue();
  }

  @Test
  void shouldCreateDatasetAlertByFilter() {
    DatasetEntity dataset = createDatasetEntity();
    datasetAlertRepository.createFilteredDataset(
        dataset.getId(), List.of(), OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
        OffsetDateTime.now());
    assertThat(datasetAlertRepository.countByIdDatasetId(dataset.getId())).isEqualTo(10);
  }

  @Test
  void shouldCreateDatasetAlertByLabelFilter() {
    DatasetEntity dataset = createDatasetEntity();
    datasetAlertRepository.createFilteredDataset(
        dataset.getId(), List.of(), OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
        OffsetDateTime.now());
    assertThat(datasetAlertRepository.countByIdDatasetId(dataset.getId())).isEqualTo(10);
  }

  @Test
  void shouldCreateEmptyDatasetAlertByFilter() {
    DatasetEntity dataset = createDatasetEntity();
    datasetAlertRepository.createFilteredDataset(
        dataset.getId(), List.of("labelvalue"), OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
        OffsetDateTime.now());
    assertThat(datasetAlertRepository.countByIdDatasetId(dataset.getId())).isEqualTo(2);
  }


  @NotNull
  private DatasetEntity createDatasetEntity() {
    var dataset = DatasetEntity.builder()
        .build();
    datasetRepository.save(dataset);
    return dataset;
  }

  private void createDatasetAlertsEntity(long datasetId) {
    for (long i = 1; i <= 10; i++) {
      var datasetAlert = DatasetFixture.newDatasetAlertEntity(DatasetAlertKey.builder()
          .datasetId(datasetId)
          .alertId(i)
          .build());
      datasetAlertRepository.save(datasetAlert);
    }
  }
}
