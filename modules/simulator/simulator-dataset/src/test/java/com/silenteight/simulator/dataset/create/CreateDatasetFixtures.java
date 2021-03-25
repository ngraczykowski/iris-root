package com.silenteight.simulator.dataset.create;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;
import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class CreateDatasetFixtures {

  static final UUID DATASET_ID = fromString("8908d28d-8129-4b6c-b0d6-3fe6be13cc45");
  static final String NAME = "Dataset name";
  static final String DESCRIPTION = "Dataset description";
  public static final OffsetDateTime DATE_1 = OffsetDateTime.of(2019, 1, 1, 0, 0, 0, 0, UTC);
  public static final OffsetDateTime DATE_2 = OffsetDateTime.of(2020, 10, 10, 23, 59, 59, 0, UTC);

  static final CreateDatasetRequest CREATE_DATASET_REQUEST = CreateDatasetRequest.builder()
      .datasetId(DATASET_ID)
      .name(NAME)
      .description(DESCRIPTION)
      .query(AlertSelectionCriteriaDto.builder()
          .alertGenerationDate(dateRange(DATE_1, DATE_2))
          .build())
      .build();

  private static RangeQueryDto dateRange(OffsetDateTime from, OffsetDateTime to) {
    return RangeQueryDto.builder()
        .from(from)
        .to(to)
        .build();
  }
}
