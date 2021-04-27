package com.silenteight.simulator.dataset.fixture;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.simulator.dataset.domain.DatasetState;
import com.silenteight.simulator.dataset.dto.AlertSelectionCriteriaDto;
import com.silenteight.simulator.dataset.dto.DatasetDto;
import com.silenteight.simulator.dataset.dto.RangeQueryDto;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.silenteight.simulator.dataset.domain.DatasetState.CURRENT;
import static java.time.ZoneOffset.UTC;
import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DatasetFixtures {

  public static final UUID DATASET_ID = fromString("b4708d8c-4832-6fde-8dc0-d17b4708d8ca");
  public static final String RESOURCE_NAME = "datasets/" + DATASET_ID.toString();
  public static final String NAME = "Dataset name";
  public static final String DESCRIPTION = "Dataset description";
  public static final UUID EXTERNAL_DATASET_ID = fromString("b6855a6f-fc63-422f-84a7-677a0c8f9a9a");
  public static final String EXTERNAL_RESOURCE_NAME = "datasets/" + EXTERNAL_DATASET_ID.toString();
  public static final DatasetState STATE = CURRENT;
  public static final long ALERTS_COUNT = 5L;
  public static final OffsetDateTime FROM = OffsetDateTime.of(2019, 1, 1, 0, 0, 0, 0, UTC);
  public static final OffsetDateTime TO = OffsetDateTime.of(2020, 10, 10, 23, 59, 59, 0, UTC);
  public static final OffsetDateTime CREATED_AT =
      OffsetDateTime.of(2021, 03, 12, 11, 25, 10, 0, UTC);
  public static final String CREATED_BY = "asmith";

  public static final DatasetDto DATASET_DTO = DatasetDto
      .builder()
      .id(DATASET_ID)
      .name(RESOURCE_NAME)
      .datasetName(NAME)
      .description(DESCRIPTION)
      .state(STATE)
      .alertsCount(ALERTS_COUNT)
      .query(query(FROM, TO))
      .createdAt(CREATED_AT)
      .createdBy(CREATED_BY)
      .build();

  private static AlertSelectionCriteriaDto query(OffsetDateTime from, OffsetDateTime to) {
    return AlertSelectionCriteriaDto.builder()
        .alertGenerationDate(dateRange(from, to))
        .build();
  }

  private static RangeQueryDto dateRange(OffsetDateTime from, OffsetDateTime to) {
    return RangeQueryDto.builder()
        .from(from)
        .to(to)
        .build();
  }
}
