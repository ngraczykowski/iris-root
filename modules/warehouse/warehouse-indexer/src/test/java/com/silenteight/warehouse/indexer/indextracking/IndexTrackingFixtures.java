package com.silenteight.warehouse.indexer.indextracking;

import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

import static java.time.OffsetDateTime.parse;

@NoArgsConstructor
public final class IndexTrackingFixtures {

  static final String DISCRIMINATOR_NEW = "123";
  static final String DISCRIMINATOR_OLD = "124";
  static final OffsetDateTime CURRENT_DATETIME = parse("2021-07-22T12:17:37.098Z");
}
