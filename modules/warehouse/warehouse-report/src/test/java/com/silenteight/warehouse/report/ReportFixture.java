package com.silenteight.warehouse.report;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import static java.sql.Timestamp.valueOf;
import static java.time.LocalDate.of;
import static java.time.LocalTime.MAX;
import static java.time.LocalTime.MIDNIGHT;
import static java.time.OffsetDateTime.of;
import static java.time.OffsetDateTime.parse;
import static java.time.ZoneOffset.UTC;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReportFixture {

  public static final LocalDate LOCAL_DATE_FROM = of(2020, 10, 15);
  public static final LocalDate LOCAL_DATE_TO = of(2021, 10, 15);
  public static final OffsetDateTime OFFSET_DATE_TIME_FROM = of(LOCAL_DATE_FROM, MIDNIGHT, UTC);
  public static final OffsetDateTime OFFSET_DATE_TIME_TO =
      of(LOCAL_DATE_TO, MAX.minusSeconds(1), UTC);

  public static final String DISCRIMINATOR = "TEST[AAAGLOBAL186R1038]_81596ace";
  public static final String NAME = "alerts/123";
  public static final Timestamp RECOMMENDATION_DATE =
      valueOf(parse("2021-01-12T10:00:37.000Z").atZoneSameInstant(UTC).toLocalDateTime());
  public static final String PAYLOAD = "{\"fvSignature\": \"9HzsNs1bv\"}";
}
