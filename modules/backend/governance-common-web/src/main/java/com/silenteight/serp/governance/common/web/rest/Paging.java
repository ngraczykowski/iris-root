package com.silenteight.serp.governance.common.web.rest;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;

@Data
@RequiredArgsConstructor
public class Paging {

  @Min(0)
  private final int pageIndex;
  @Min(1)
  private final int pageSize;

  public long getSkip() {
    return (long) pageIndex * pageSize;
  }
}
