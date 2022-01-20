package com.silenteight.warehouse.indexer.alert.mapping;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class AlertMapperConstants {

  public static final String ALERT_PREFIX = "alert_";
  public static final String ALERT_NAME = "s8_alert_name";
  public static final String DISCRIMINATOR = "s8_discriminator";
  public static final String INDEX_TIMESTAMP = "index_timestamp";
  public static final String QA_LEVEL_0_STATE = "qa.level-0.state";
  public static final String QA_LEVEL_0_COMMENT = "qa.level-0.comment";
}
