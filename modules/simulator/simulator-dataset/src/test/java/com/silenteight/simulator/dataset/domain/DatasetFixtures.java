package com.silenteight.simulator.dataset.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class DatasetFixtures {

  static final String DATASET_NAME = "datasets/b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  static final String NAME = "Dataset name";
  static final String DESCRIPTION = "Dataset description";
  static final long ALERTS_COUNT = 5L;
  static final OffsetDateTime FROM = OffsetDateTime.of(2019, 1, 1, 0, 0, 0, 0, UTC);
  static final OffsetDateTime TO = OffsetDateTime.of(2020, 10, 10, 23, 59, 59, 0, UTC);
  static final String CREATED_BY = "asmith";
}
