package com.silenteight.serp.governance.qa;

import java.time.OffsetDateTime;
import java.util.UUID;

import static java.time.OffsetDateTime.parse;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

public final class AlertFixture {

  private AlertFixture() {
    
  }

  public static final long ALERT_ID = 1L;
  public static final String ALERT_CREATED_AT_FORMAT = "2020-05-20T01:01:01+01:00";
  public static final OffsetDateTime ALERT_CREATED_AT =
      parse(ALERT_CREATED_AT_FORMAT, ISO_OFFSET_DATE_TIME);
  public static final String DISCRIMINATOR =
      "CR769FINAL068:SG:LR-EAML:1040502_cdf2433cf535829966563030fe72e438";
  public static final String DISCRIMINATOR_2 =
      "CR769FINAL068:SG:LR-EAML:1040502_96a48ce8-f7c6-4934-b112-990ebf3585b5";

  public static String generateDiscriminator() {
    return "CR769FINAL068:SG:LR-EAML:1040502_" + UUID.randomUUID();
  }
}
