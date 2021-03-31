package com.silenteight.simulator.dataset.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.simulator.dataset.dto.DatasetDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.silenteight.simulator.dataset.domain.DatasetFixtures.*;
import static com.silenteight.simulator.dataset.domain.DatasetState.CURRENT;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { DatasetTestConfiguration.class })
class DatasetQueryTest extends BaseDataJpaTest {

  @Autowired
  DatasetQuery underTest;

  @Autowired
  DatasetEntityRepository repository;

  @Test
  void shouldListDatasets() {
    persistDataset();

    List<DatasetDto> result = underTest.list(CURRENT);

    assertThat(result).hasSize(1);
    DatasetDto datasetDto = result.get(0);
    assertThat(datasetDto.getDatasetName()).isEqualTo(DATASET_NAME);
    assertThat(datasetDto.getName()).isEqualTo(NAME);
    assertThat(datasetDto.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(datasetDto.getAlertsCount()).isEqualTo(ALERTS_COUNT);
    assertThat(datasetDto.getCreatedBy()).isEqualTo(CREATED_BY);
    assertThat(datasetDto.getQuery()).isNotNull();
  }

  private void persistDataset() {
    DatasetEntity datasetEntity = DatasetEntity.builder()
        .datasetName(DATASET_NAME)
        .createdBy(CREATED_BY)
        .name(NAME)
        .description(DESCRIPTION)
        .initialAlertCount(ALERTS_COUNT)
        .state(CURRENT)
        .generationDateFrom(FROM)
        .generationDateTo(TO)
        .build();

    repository.save(datasetEntity);
  }
}
