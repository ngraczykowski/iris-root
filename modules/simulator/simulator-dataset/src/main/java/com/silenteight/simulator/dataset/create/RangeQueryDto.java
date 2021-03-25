package com.silenteight.simulator.dataset.create;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;

@Value
@Builder
public class RangeQueryDto {

  @NonNull
  OffsetDateTime from;

  @NonNull
  OffsetDateTime to;
}
