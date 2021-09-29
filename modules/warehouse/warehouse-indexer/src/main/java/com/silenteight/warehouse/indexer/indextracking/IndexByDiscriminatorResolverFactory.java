package com.silenteight.warehouse.indexer.indextracking;

import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
public class IndexByDiscriminatorResolverFactory {

  @NotNull
  private final ProductionAlertTrackingService productionAlertTrackingService;

  public IndexByDiscriminatorResolver productionWriteIndexProvider() {
    return new IndexByDiscriminatorResolver(productionAlertTrackingService);
  }
}
