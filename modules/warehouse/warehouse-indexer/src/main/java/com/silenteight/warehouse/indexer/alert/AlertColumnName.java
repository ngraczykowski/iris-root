package com.silenteight.warehouse.indexer.alert;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents column name in warehouse_alert table
 */
@AllArgsConstructor
@Getter
public enum AlertColumnName {
  CREATED_AT("created_at"),
  RECOMMENDATION_DATE("recommendation_date");

  private final String name;
}
