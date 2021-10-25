package com.silenteight.warehouse.indexer.production.indextracking;

import lombok.AllArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
class ProductionAlertNamingStrategy {

  @NotNull
  private final TimeSource timeSource;

  @NotNull
  private final String environmentPrefix;

  public String getElasticWriteIndexName() {
    String utcDate = timeSource
        .localDateTime()
        .atZone(ZoneOffset.UTC)
        .format(DateTimeFormatter.ISO_LOCAL_DATE);

    return environmentPrefix + "_production_alert." + utcDate;
  }
}
