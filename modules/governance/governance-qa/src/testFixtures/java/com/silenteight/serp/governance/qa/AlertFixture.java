package com.silenteight.serp.governance.qa;

import java.time.OffsetDateTime;
import java.util.UUID;

import static java.time.OffsetDateTime.parse;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

public final class AlertFixture {

  private AlertFixture() {

  }

  public static final String ALERT_ID = "a7e28d2f-c708-490c-837c-b16ffbed8420";
  public static final String ALERT_CREATED_AT_FORMAT = "2020-05-20T01:01:01+01:00";
  public static final OffsetDateTime ALERT_CREATED_AT =
      parse(ALERT_CREATED_AT_FORMAT, ISO_OFFSET_DATE_TIME);
  public static final String ALERT_NAME = "alerts/a7e28d2f-c708-490c-837c-b16ffbed8420";
  public static final String ALERT_NAME_2 = "alerts/96a48ce8-f7c6-4934-b112-990ebf3585b5";

  public static String generateAlertName() {
    return "alerts/" + UUID.randomUUID();
  }
}
