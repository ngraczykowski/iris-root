package com.silenteight.warehouse.report;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

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

  public static final String DISCRIMINATOR_1 = "TEST[AAAGLOBAL186R1038]_81596ace";
  public static final String DISCRIMINATOR_2 = "TEST[AAAGLOBAL186R1038]_3218Arra";
  public static final String DISCRIMINATOR_3 = "TEST[AAAGLOBAL186R1038]_1113ACca";
  public static final String NAME_1 = "alerts/123";
  public static final String NAME_2 = "alerts/456";
  public static final String NAME_3 = "alerts/789";
  public static final LocalDateTime RECOMMENDATION_DATE =
      parse("2021-01-12T10:00:37.000Z").atZoneSameInstant(UTC).toLocalDateTime();
  public static final String PAYLOAD_KEY_COUNTRY = "country";
  public static final String COUNTRY_PL = "PL";
  public static final String COUNTRY_US = "US";
  public static final String PAYLOAD_KEY_SIGNATURE = "fvSignature";
  public static final String PAYLOAD_VALUE_SIGNATURE = "9HzsNs1bv";
  public static final String PAYLOAD_KEY_COMMENT = "comment";
  public static final String PAYLOAD_VALUE_COMMENT_WITH_NEWLINE = "Comment \n with \n newline";

  public static final String PAYLOAD_VALUE_COMMENT_LONG =
      """
          S8 recommended action: False Positive\\n\\nSAN 2485278: Alerted Party's name (AO A C also known as ????????? | ???????????????) does not match Watchlist Party name (XXXYYY AO). Alerted Party's incorporation country (\\"CN\\", CHINA, CN) does not match Watchlist Party country (XXXYYY, XXXYYY FEDERATION). Alerted Party's registration country (CHINA, \\"CN\\", CN) does not match Watchlist Party country (XXXYYY FEDERATION). Alerted Party's other countries (CHINA, CN) do not match Watchlist Party countries (XXXYYY, XXXYYY FEDERATION).
      """;
  public static final String COUNTRY_GROUP = "b4df80da-7309-4982-9c03-b14abf93d0b5";
  public static final UUID COUNTRY_GROUP_UUID = UUID.fromString(COUNTRY_GROUP);
}
