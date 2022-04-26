package com.silenteight.simulator.dataset.domain;

import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.simulator.dataset.dto.DatasetDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static com.silenteight.simulator.dataset.domain.DatasetState.ACTIVE;
import static com.silenteight.simulator.dataset.domain.DatasetState.ARCHIVED;
import static com.silenteight.simulator.dataset.domain.DatasetState.EXPIRED;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.*;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { DatasetConfiguration.class })
class DatasetQueryTest extends BaseDataJpaTest {

  @Autowired
  DatasetQuery underTest;

  @Autowired
  DatasetEntityRepository repository;

  @ParameterizedTest
  @MethodSource("getDatasetsSearchCriteria")
  void shouldListDatasetsByStates(Set<DatasetState> states, Set<UUID> datasetIds) {
    // given
    persistDatasets();

    // when
    List<DatasetDto> result = underTest.list(states);

    // then
    assertThat(result).hasSize(datasetIds.size());
    assertThat(result.stream().map(DatasetDto::getState).collect(toList())).containsAll(states);
    assertThat(result.stream().map(DatasetDto::getId).collect(toList())).containsAll(datasetIds);
    DatasetDto datasetDto = result.get(0);
    assertThat(datasetDto.getDatasetName()).isEqualTo(DATASET_NAME);
    assertThat(datasetDto.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(datasetDto.getAlertsCount()).isEqualTo(ALERTS_COUNT);
    assertThat(datasetDto.getCreatedBy()).isEqualTo(CREATED_BY);
    assertThat(datasetDto.getQuery()).isNotNull();
    assertThat(datasetDto.getQuery().getCountries()).containsExactlyElementsOf(COUNTRIES);
  }

  @Test
  void shouldGetDataset() {
    // given
    persistDataset(ID_1, ACTIVE);

    // when
    DatasetDto result = underTest.get(ID_1);

    // then
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
    // given
    persistDataset(ID_1, ACTIVE);

    // when
    String result = underTest.getExternalResourceName(ID_1);

    // then
    assertThat(result).isEqualTo(EXTERNAL_RESOURCE_NAME);
  }

  @Test
  void shouldGetDatasetNames() {
    // given
    persistDataset(ID_1, ACTIVE);

    // when
    Collection<String> result = underTest.getDatasetNames(List.of(EXTERNAL_RESOURCE_NAME));

    // then
    assertThat(result).isEqualTo(List.of(RESOURCE_NAME_1));
  }

  private void persistDatasets() {
    persistDataset(ID_1, ACTIVE);
    persistDataset(ID_2, ACTIVE);
    persistDataset(ID_3, ARCHIVED);
    persistDataset(ID_4, ARCHIVED);
    persistDataset(ID_5, ARCHIVED);
    persistDataset(ID_6, EXPIRED);
  }

  private void persistDataset(UUID datasetId, DatasetState state) {
    repository.save(buildEntity(datasetId, state));
  }

  private static Stream<Arguments> getDatasetsSearchCriteria() {
    return Stream.of(
        Arguments.of(Set.of(ACTIVE), Set.of(ID_1, ID_2)),
        Arguments.of(Set.of(ARCHIVED), Set.of(ID_3, ID_4, ID_5)),
        Arguments.of(Set.of(EXPIRED), Set.of(ID_6), 1),
        Arguments.of(Set.of(ACTIVE, ARCHIVED), Set.of(ID_1, ID_2, ID_3, ID_4, ID_5)));
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
