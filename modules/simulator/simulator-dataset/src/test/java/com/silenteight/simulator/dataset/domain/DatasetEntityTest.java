package com.silenteight.simulator.dataset.domain;

import com.silenteight.simulator.dataset.domain.exception.DatasetNotInProperStateException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static com.silenteight.simulator.dataset.domain.DatasetState.ACTIVE;
import static com.silenteight.simulator.dataset.domain.DatasetState.ARCHIVED;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.CREATED_BY;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.DATASET_NAME;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.EXTERNAL_RESOURCE_NAME;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.ID_1;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;

class DatasetEntityTest {

  @Test
  void shouldArchive() {
    // given
    DatasetEntity dataset = createDatasetEntity(ACTIVE);

    // when
    dataset.archive();

    // then
    assertThat(dataset.getState()).isEqualTo(ARCHIVED);
  }

  @ParameterizedTest
  @EnumSource(value = DatasetState.class, names = { "ARCHIVED" })
  void shouldThrowExceptionWhenNotInStateForArchive(DatasetState state) {
    // given
    DatasetEntity dataset = createDatasetEntity(state);

    // then
    assertThatThrownBy(
        dataset::archive)
        .isInstanceOf(DatasetNotInProperStateException.class)
        .hasMessage(format("Dataset should be in state: %s.", ACTIVE));
  }

  private DatasetEntity createDatasetEntity(DatasetState state) {
    return DatasetEntity.builder()
        .datasetId(ID_1)
        .name(DATASET_NAME)
        .externalResourceName(EXTERNAL_RESOURCE_NAME)
        .createdBy(CREATED_BY)
        .state(state)
        .build();
  }
}
