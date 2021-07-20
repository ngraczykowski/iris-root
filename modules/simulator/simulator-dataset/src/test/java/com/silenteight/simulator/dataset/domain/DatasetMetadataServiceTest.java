package com.silenteight.simulator.dataset.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.silenteight.simulator.dataset.domain.DatasetState.CURRENT;
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
    assertThat(dataset.getState()).isEqualTo(CURRENT);
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

  private void persistDataset(UUID datasetId, long initialAlertCount) {
    DatasetEntity datasetEntity = DatasetEntity.builder()
        .datasetId(datasetId)
        .createdBy(CREATED_BY)
        .name(DATASET_NAME)
        .description(DESCRIPTION)
        .externalResourceName(EXTERNAL_RESOURCE_NAME)
        .initialAlertCount(initialAlertCount)
        .state(CURRENT)
        .generationDateFrom(FROM)
        .generationDateTo(TO)
        .countries(COUNTRIES)
        .build();

    repository.save(datasetEntity);
  }
}
