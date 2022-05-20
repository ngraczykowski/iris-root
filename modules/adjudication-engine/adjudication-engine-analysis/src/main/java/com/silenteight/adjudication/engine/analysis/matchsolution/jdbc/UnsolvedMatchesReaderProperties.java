package com.silenteight.adjudication.engine.analysis.matchsolution.jdbc;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@ConfigurationProperties(prefix = "ae.analysis.match-solution.unsolved-matches-reader")
@Data
@Validated
class UnsolvedMatchesReaderProperties {

  static final int DEFAULT_CHUNK_SIZE = 1_024;
  static final int DEFAULT_MAX_ROWS = 8_192;

  @Min(1)
  @Max(1_048_576)
  private int chunkSize = DEFAULT_CHUNK_SIZE;

  @Min(1)
  @Max(1_048_576)
  private int maxRows = DEFAULT_MAX_ROWS;
}
