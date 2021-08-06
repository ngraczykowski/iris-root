package com.silenteight.simulator.dataset.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.simulator.dataset.dto.DatasetDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.silenteight.simulator.dataset.domain.DatasetState.ACTIVE;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.*;
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

    List<DatasetDto> result = underTest.list(ACTIVE);

    assertThat(result).hasSize(1);
    DatasetDto datasetDto = result.get(0);
    assertThat(datasetDto.getId()).isEqualTo(ID);
    assertThat(datasetDto.getName()).isEqualTo("datasets/" + ID);
    assertThat(datasetDto.getDatasetName()).isEqualTo(DATASET_NAME);
    assertThat(datasetDto.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(datasetDto.getAlertsCount()).isEqualTo(ALERTS_COUNT);
    assertThat(datasetDto.getCreatedBy()).isEqualTo(CREATED_BY);
    assertThat(datasetDto.getQuery()).isNotNull();
    assertThat(datasetDto.getQuery().getCountries()).containsExactlyElementsOf(COUNTRIES);
  }

  @Test
  void shouldGetDataset() {
    persistDataset();

    DatasetDto result = underTest.get(ID);

    assertThat(result.getId()).isEqualTo(ID);
    assertThat(result.getName()).isEqualTo("datasets/" + ID);
    assertThat(result.getDatasetName()).isEqualTo(DATASET_NAME);
    assertThat(result.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(result.getAlertsCount()).isEqualTo(ALERTS_COUNT);
    assertThat(result.getCreatedBy()).isEqualTo(CREATED_BY);
    assertThat(result.getQuery()).isNotNull();
    assertThat(result.getQuery().getCountries()).containsExactlyElementsOf(COUNTRIES);
  }

  @Test
  void shouldGetExternalResourceName() {
    persistDataset();

    String result = underTest.getExternalResourceName(ID);

    assertThat(result).isEqualTo(EXTERNAL_RESOURCE_NAME);
  }

  private void persistDataset() {
    DatasetEntity datasetEntity = DatasetEntity.builder()
        .datasetId(ID)
        .createdBy(CREATED_BY)
        .name(DATASET_NAME)
        .description(DESCRIPTION)
        .externalResourceName(EXTERNAL_RESOURCE_NAME)
        .initialAlertCount(ALERTS_COUNT)
        .state(ACTIVE)
        .generationDateFrom(FROM)
        .generationDateTo(TO)
        .countries(COUNTRIES)
        .build();

    repository.save(datasetEntity);
  }
}
