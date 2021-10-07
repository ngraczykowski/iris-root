package com.silenteight.warehouse.indexer.simulation.analysis;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import javax.annotation.concurrent.ThreadSafe;

@Value
@Builder
@ThreadSafe
public class NewSimulationAnalysisEvent {

  private static final long serialVersionUID = -5302121986943664724L;

  @NonNull
  @Default
  OffsetDateTime date = OffsetDateTime.now();
  @NonNull
  String analysis;
  @NonNull
  AnalysisMetadataDto analysisMetadataDto;
}
