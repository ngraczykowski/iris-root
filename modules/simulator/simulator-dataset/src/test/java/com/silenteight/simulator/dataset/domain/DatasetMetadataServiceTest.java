package com.silenteight.simulator.dataset.domain;

import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.simulator.dataset.create.CreateDatasetRequest;
import com.silenteight.simulator.dataset.domain.exception.DatasetNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static com.silenteight.simulator.dataset.domain.AlertMatch.MULTI;
import static com.silenteight.simulator.dataset.domain.AlertMatch.SINGLE;
import static com.silenteight.simulator.dataset.domain.DatasetState.ACTIVE;
import static com.silenteight.simulator.dataset.domain.DatasetState.ARCHIVED;
import static com.silenteight.simulator.dataset.domain.DatasetState.EXPIRED;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.*;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = DatasetConfiguration.class)
class DatasetMetadataServiceTest extends BaseDataJpaTest {

  @Autowired
  DatasetMetadataService underTest;

  @Autowired
  DatasetEntityRepository repository;

  @ParameterizedTest
  @MethodSource("getCreateDataSetRequestList")
  void createMetadata(
      CreateDatasetRequest request, AlertMatch expectedAlertMatchLevel) {
    // when
    underTest.createMetadata(request, DATASET);

    // then
    Optional<DatasetEntity> datasetOpt = repository.findByDatasetId(ID_1);
    assertThat(datasetOpt).isPresent();
    DatasetEntity dataset = datasetOpt.get();
    assertThat(dataset.getDatasetId()).isEqualTo(ID_1);
    assertThat(dataset.getState()).isEqualTo(ACTIVE);
    assertThat(dataset.getAlertMatch()).isEqualTo(expectedAlertMatchLevel);
    assertThat(dataset.getExternalResourceName()).isEqualTo(EXTERNAL_RESOURCE_NAME);
  }

  @Test
  void countAllAlerts() {
    // given
    persistDataset(ID_1, ALERTS_COUNT);
    persistDataset(ID_2, SECOND_ALERTS_COUNT);

    // when
    long result = underTest.countAllAlerts(Set.of(RESOURCE_NAME_1, RESOURCE_NAME_2));

    // then
    assertThat(result).isEqualTo(ALERTS_COUNT + SECOND_ALERTS_COUNT);
  }

  @Test
  void shouldArchive() {
    // given
    DatasetEntity dataset = persistDataset(ID_1, ALERTS_COUNT);

    // when
    underTest.archive(ID_1);

    // then
    DatasetEntity savedDataset = entityManager.find(DatasetEntity.class, dataset.getId());
    assertThat(savedDataset.getState()).isEqualTo(ARCHIVED);
  }

  @Test
  void shouldThrowIfArchivingAndDatasetNotFound() {
    assertThatThrownBy(() -> underTest.archive(ID_1))
        .isInstanceOf(DatasetNotFoundException.class)
        .hasMessageContaining("datasetId=" + ID_1);
  }

  @Test
  void shouldExpire() {
    // given
    DatasetEntity dataset = persistDataset(ID_1, ALERTS_COUNT);

    // when
    underTest.expire(List.of(EXTERNAL_RESOURCE_NAME));

    // then
    DatasetEntity savedDataset = entityManager.find(DatasetEntity.class, dataset.getId());
    assertThat(savedDataset.getState()).isEqualTo(EXPIRED);
  }

  private static Stream<Arguments> getCreateDataSetRequestList() {
    return Stream.of(
        Arguments.of(makeCreateDatasetRequest(true), MULTI),
        Arguments.of(
            makeCreateDatasetRequest(false), SINGLE)
    );
  }

  private static CreateDatasetRequest makeCreateDatasetRequest(boolean useMultiHitAlerts) {

    return CreateDatasetRequest.builder()
        .id(ID_1)
        .datasetName(DATASET_NAME)
        .description(DESCRIPTION)
        .rangeFrom(FROM)
        .rangeTo(TO)
        .createdBy(CREATED_BY)
        .useMultiHitAlerts(useMultiHitAlerts)
        .build();
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
        .labels(JsonConversionHelper.INSTANCE.serializeToString(LABELS))
        .alertMatch(MULTI)
        .build();

    return repository.save(datasetEntity);
  }
}
