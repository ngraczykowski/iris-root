package com.silenteight.sens.webapp.common.query;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@RequiredArgsConstructor
public class PageableResult<T> {

  @NonNull
  private final List<T> results;
  private final long total;

  public PageableResult() {
    results = Collections.emptyList();
    total = 0;
  }
}
