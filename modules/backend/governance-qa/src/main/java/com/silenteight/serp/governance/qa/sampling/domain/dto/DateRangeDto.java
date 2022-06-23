package com.silenteight.serp.governance.qa.sampling.domain.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import javax.validation.constraints.NotNull;

@Data
public class DateRangeDto {

  @NotNull
  private final OffsetDateTime from;
  @NotNull
  private final OffsetDateTime to;
}
