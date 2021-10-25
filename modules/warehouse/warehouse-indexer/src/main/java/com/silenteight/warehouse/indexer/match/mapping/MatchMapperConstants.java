package com.silenteight.warehouse.indexer.match.mapping;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class MatchMapperConstants {

  public static final String MATCH_PREFIX = "match_";
  public static final String MATCH_NAME = "s8_match_name";
  public static final String DISCRIMINATOR = "s8_match_discriminator";
  public static final String INDEX_TIMESTAMP = "index_timestamp";
}
