package com.silenteight.sens.webapp.backend.support;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;

@Data
@RequiredArgsConstructor
public class Paging {

  @Min(0)
  private final int offset;
  @Min(1)
  private final int limit;
}
