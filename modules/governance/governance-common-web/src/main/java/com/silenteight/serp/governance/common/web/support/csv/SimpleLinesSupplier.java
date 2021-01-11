package com.silenteight.serp.governance.common.web.support.csv;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class SimpleLinesSupplier implements LinesSupplier {

  private final List<String> lines;

  @Override
  public Stream<String> lines() {
    return lines.stream();
  }
}
