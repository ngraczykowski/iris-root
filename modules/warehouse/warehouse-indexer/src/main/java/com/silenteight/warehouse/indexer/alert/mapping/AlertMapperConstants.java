package com.silenteight.warehouse.indexer.alert.mapping;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AlertMapperConstants {

  public static final String ALERT_PREFIX = "alert_";
  public static final String ALERT_NAME = "s8_alert_name";
  public static final String DISCRIMINATOR = "s8_discriminator";
  public static final String INDEX_TIMESTAMP = "index_timestamp";
  public static final String QA_LEVEL_0_STATE = "qa.level-0.state";
  public static final String QA_LEVEL_0_COMMENT = "qa.level-0.comment";

  public static String removeAlertPrefix(String propertyName) {
    if (propertyName.startsWith(ALERT_PREFIX)) {
      return propertyName.replace(ALERT_PREFIX, "");
    }
    return propertyName;
  }
}
