package com.silenteight.simulator.dataset.create;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.simulator.dataset.dto.AlertSelectionCriteriaDto;
import com.silenteight.simulator.dataset.dto.RangeQueryDto;

import java.time.OffsetDateTime;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;
import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class CreateDatasetFixtures {

  static final UUID DATASET_ID = fromString("b4708d8c-4832-6fde-8dc0-d17b4708d8ca");
  static final String NAME = "Dataset name";
  static final String DESCRIPTION = "Dataset description";
  static final OffsetDateTime FROM = OffsetDateTime.of(2019, 1, 1, 0, 0, 0, 0, UTC);
  static final OffsetDateTime TO = OffsetDateTime.of(2020, 10, 10, 23, 59, 59, 0, UTC);
  static final String CREATED_BY = "asmith";

  static final CreateDatasetRequestDto CREATE_DATASET_REQUEST_DTO =
      CreateDatasetRequestDto.builder()
          .id(DATASET_ID)
          .name(NAME)
          .description(DESCRIPTION)
          .query(query(FROM, TO))
          .build();

  static final CreateDatasetRequest CREATE_DATASET_REQUEST =
      CreateDatasetRequest.builder()
          .id(DATASET_ID)
          .name(NAME)
          .description(DESCRIPTION)
          .query(query(FROM, TO))
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
