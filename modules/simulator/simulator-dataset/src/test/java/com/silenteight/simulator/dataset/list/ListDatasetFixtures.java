package com.silenteight.simulator.dataset.list;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.simulator.dataset.domain.DatasetState;
import com.silenteight.simulator.dataset.dto.AlertSelectionCriteriaDto;
import com.silenteight.simulator.dataset.dto.DatasetDto;
import com.silenteight.simulator.dataset.dto.RangeQueryDto;

import java.time.OffsetDateTime;

import static com.silenteight.simulator.dataset.domain.DatasetState.CURRENT;
import static java.time.ZoneOffset.UTC;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ListDatasetFixtures {

  static final String DATASET_NAME = "datasets/b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  static final String NAME = "Dataset name";
  static final String DESCRIPTION = "Dataset description";
  static final DatasetState STATE = CURRENT;
  static final long ALERTS_COUNT = 5L;
  static final OffsetDateTime FROM = OffsetDateTime.of(2019, 1, 1, 0, 0, 0, 0, UTC);
  static final OffsetDateTime TO = OffsetDateTime.of(2020, 10, 10, 23, 59, 59, 0, UTC);
  static final OffsetDateTime CREATED_AT = OffsetDateTime.of(2021, 03, 12, 11, 25, 10, 0, UTC);
  static final String CREATED_BY = "asmith";

  static final DatasetDto DATASET_DTO = DatasetDto.builder()
      .datasetName(DATASET_NAME)
      .name(NAME)
      .description(DESCRIPTION)
      .state(STATE)
      .alertsCount(ALERTS_COUNT)
      .query(AlertSelectionCriteriaDto.builder()
          .alertGenerationDate(dateRange(FROM, TO))
          .build())
      .createdAt(CREATED_AT)
      .createdBy(CREATED_BY)
      .build();

  private static RangeQueryDto dateRange(OffsetDateTime from, OffsetDateTime to) {
    return RangeQueryDto.builder()
        .from(from)
        .to(to)
        .build();
  }
}
