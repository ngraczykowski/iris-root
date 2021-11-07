package com.silenteight.simulator.dataset.domain;

import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.simulator.dataset.dto.DatasetDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.silenteight.simulator.dataset.domain.DatasetState.ACTIVE;
import static com.silenteight.simulator.dataset.domain.DatasetState.ARCHIVED;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.*;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { DatasetTestConfiguration.class })
class DatasetQueryTest extends BaseDataJpaTest {

  @Autowired
  DatasetQuery underTest;

  @Autowired
  DatasetEntityRepository repository;

  @BeforeEach
  void setup() {
    persistDatasets();
  }

  @ParameterizedTest
  @MethodSource("getDatasetsSearchCriteria")
  void shouldListDatasetsByState(DatasetState state, UUID datasetId, int expectedNumberOfDatasets) {
    List<DatasetDto> result = underTest.list(state);

    assertThat(result).hasSize(expectedNumberOfDatasets);
    assertThat(result.stream().map(DatasetDto::getState).collect(toList())).containsOnly(state);
    DatasetDto datasetDto = result.get(0);
    assertThat(datasetDto.getId()).isEqualTo(datasetId);
    assertThat(datasetDto.getName()).isEqualTo("datasets/" + datasetId);
    assertThat(datasetDto.getDatasetName()).isEqualTo(DATASET_NAME);
    assertThat(datasetDto.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(datasetDto.getAlertsCount()).isEqualTo(ALERTS_COUNT);
    assertThat(datasetDto.getCreatedBy()).isEqualTo(CREATED_BY);
    assertThat(datasetDto.getQuery()).isNotNull();
    assertThat(datasetDto.getQuery().getCountries()).containsExactlyElementsOf(COUNTRIES);
  }

  @Test
  void shouldGetDataset() {
    DatasetDto result = underTest.get(ID_1);

    assertThat(result.getId()).isEqualTo(ID_1);
    assertThat(result.getName()).isEqualTo("datasets/" + ID_1);
    assertThat(result.getDatasetName()).isEqualTo(DATASET_NAME);
    assertThat(result.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(result.getAlertsCount()).isEqualTo(ALERTS_COUNT);
    assertThat(result.getCreatedBy()).isEqualTo(CREATED_BY);
    assertThat(result.getQuery()).isNotNull();
    assertThat(result.getQuery().getCountries()).containsExactlyElementsOf(COUNTRIES);
  }

  @Test
  void shouldGetExternalResourceName() {
    String result = underTest.getExternalResourceName(ID_1);

    assertThat(result).isEqualTo(EXTERNAL_RESOURCE_NAME);
  }

  private void persistDatasets() {
    repository.save(buildEntity(ID_1, ACTIVE));
    repository.save(buildEntity(ID_2, ACTIVE));
    repository.save(buildEntity(ID_3, ARCHIVED));
    repository.save(buildEntity(ID_4, ARCHIVED));
    repository.save(buildEntity(ID_5, ARCHIVED));
  }

  private static Stream<Arguments> getDatasetsSearchCriteria() {
    return Stream.of(
        Arguments.of(ACTIVE, ID_1, 2),
        Arguments.of(ARCHIVED, ID_3, 3)
    );
  }

  private DatasetEntity buildEntity(UUID datasetId, DatasetState state) {
    return DatasetEntity.builder()
        .datasetId(datasetId)
        .createdBy(CREATED_BY)
        .name(DATASET_NAME)
        .description(DESCRIPTION)
        .externalResourceName(EXTERNAL_RESOURCE_NAME)
        .initialAlertCount(ALERTS_COUNT)
        .state(state)
        .generationDateFrom(FROM)
        .generationDateTo(TO)
        .labels(JsonConversionHelper.INSTANCE.serializeToString(LABELS))
        .build();
  }
}
