package com.silenteight.sens.webapp.backend.support;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class Paging {

  @Min(0)
  private int offset;
  @Min(1)
  private int limit;
}
