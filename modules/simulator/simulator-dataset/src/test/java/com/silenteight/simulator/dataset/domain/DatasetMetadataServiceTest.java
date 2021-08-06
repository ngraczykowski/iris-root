package com.silenteight.simulator.dataset.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.simulator.dataset.domain.exception.DatasetNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.silenteight.simulator.dataset.domain.DatasetState.ACTIVE;
import static com.silenteight.simulator.dataset.domain.DatasetState.ARCHIVED;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.*;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { DatasetTestConfiguration.class })
class DatasetMetadataServiceTest extends BaseDataJpaTest {

  @Autowired
  DatasetMetadataService underTest;

  @Autowired
  DatasetEntityRepository repository;

  @Test
  void createMetadata() {
    // when
    underTest.createMetadata(CREATE_DATASET_REQUEST, DATASET);

    // then
    Optional<DatasetEntity> datasetOpt = repository.findByDatasetId(ID);
    assertThat(datasetOpt).isPresent();
    DatasetEntity dataset = datasetOpt.get();
    assertThat(dataset.getDatasetId()).isEqualTo(ID);
    assertThat(dataset.getState()).isEqualTo(ACTIVE);
    assertThat(dataset.getExternalResourceName()).isEqualTo(EXTERNAL_RESOURCE_NAME);
  }

  @Test
  void countAllAlerts() {
    // given
    persistDataset(ID, ALERTS_COUNT);
    persistDataset(SECOND_ID, SECOND_ALERTS_COUNT);

    // when
    long result = underTest.countAllAlerts(Set.of(RESOURCE_NAME, SECOND_RESOURCE_NAME));

    // then
    assertThat(result).isEqualTo(ALERTS_COUNT + SECOND_ALERTS_COUNT);
  }

  @Test
  void shouldArchive() {
    // given
    DatasetEntity dataset = persistDataset(ID, ALERTS_COUNT);

    // when
    underTest.archive(ID);

    // then
    DatasetEntity savedDataset = entityManager.find(DatasetEntity.class, dataset.getId());
    assertThat(savedDataset.getState()).isEqualTo(ARCHIVED);
  }

  @Test
  void shouldThrowIfArchivingAndDatasetNotFound() {
    assertThatThrownBy(() -> underTest.archive(ID))
        .isInstanceOf(DatasetNotFoundException.class)
        .hasMessageContaining("datasetId=" + ID);
  }

  private DatasetEntity persistDataset(UUID datasetId, long initialAlertCount) {
    DatasetEntity datasetEntity = DatasetEntity.builder()
        .datasetId(datasetId)
        .createdBy(CREATED_BY)
        .name(DATASET_NAME)
        .description(DESCRIPTION)
        .externalResourceName(EXTERNAL_RESOURCE_NAME)
        .initialAlertCount(initialAlertCount)
        .state(ACTIVE)
        .generationDateFrom(FROM)
        .generationDateTo(TO)
        .countries(COUNTRIES)
        .build();

    return repository.save(datasetEntity);
  }
}
