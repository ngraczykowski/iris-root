package com.silenteight.warehouse.indexer.analysis;

import lombok.*;
import lombok.Builder.Default;

import java.time.OffsetDateTime;
import javax.annotation.concurrent.ThreadSafe;

@Value
@Builder
@ThreadSafe
public class NewAnalysisEvent {

  private static final long serialVersionUID = -5302121986943664724L;

  @NonNull
  @Default
  OffsetDateTime date = OffsetDateTime.now();
  @NonNull
  String analysis;
  @NonNull
  boolean simulation;
  @NonNull
  AnalysisMetadataDto analysisMetadataDto;
}
