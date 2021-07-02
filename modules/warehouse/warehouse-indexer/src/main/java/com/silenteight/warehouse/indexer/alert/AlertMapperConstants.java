package com.silenteight.warehouse.indexer.alert;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class AlertMapperConstants {

  public static final String MATCH_PREFIX = "match_";
  public static final String ALERT_PREFIX = "alert_";
  public static final String ALERT_ID_KEY = "alert_id";
  public static final String MATCH_ID_KEY = "match_id";
  public static final String INDEX_TIMESTAMP = "index_timestamp";
}
