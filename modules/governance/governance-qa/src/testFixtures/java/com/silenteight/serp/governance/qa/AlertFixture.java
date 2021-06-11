package com.silenteight.serp.governance.qa;

import java.time.OffsetDateTime;
import java.util.UUID;

import static java.lang.String.format;
import static java.time.OffsetDateTime.parse;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.util.UUID.fromString;

public class AlertFixture {
  public static final UUID ALERT_ID = fromString("015d8483-b3b1-467f-801a-3530037f220a");
  public static final String ALERT_NAME = format("alerts/%s", ALERT_ID);
  public static final String ALERT_CREATED_AT_FORMAT = "2020-05-20T01:01:01+01:00";
  public static final OffsetDateTime ALERT_CREATED_AT =
      parse(ALERT_CREATED_AT_FORMAT, ISO_OFFSET_DATE_TIME);

  public static String generateAlertName() {
    return "alerts/" + UUID.randomUUID();
  }
}
