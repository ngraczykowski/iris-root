package com.silenteight.sens.webapp.scb.report;

import com.silenteight.sens.webapp.common.support.file.FileLineWriter;
import com.silenteight.sep.base.testing.time.MockTimeSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.sens.webapp.common.testing.matcher.StreamMatcher.streamThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthActivityReportGeneratorTest {

  private static final DateRange DEFAULT_DATE_RANGE = new DateRange(
      OffsetDateTime.now().minusHours(1), OffsetDateTime.now());

  private static final String REPORTS_DIR = "path/to/reports";

  @Mock
  private DateRangeProvider dateRangeProvider;

  @Mock
  private UserAuthActivityEventProvider userAuthActivityEventProvider;

  @Mock
  private FileLineWriter fileLineWriter;

  private UserAuthActivityReportGenerator underTest;

  @BeforeEach
  void setUp() {
    underTest = new UserAuthActivityReportGenerator(
        dateRangeProvider,
        userAuthActivityEventProvider,
        REPORTS_DIR,
        fileLineWriter,
        new MockTimeSource(Instant.now()));

    when(dateRangeProvider.latestDateRange()).thenReturn(DEFAULT_DATE_RANGE);
  }

  @Test
  void generatesReportRowsFromEvents() throws IOException {
    when(userAuthActivityEventProvider.provide(
        any(OffsetDateTime.class), any(OffsetDateTime.class)))
        .thenReturn(List.of(
            UserAuthActivityEventDto.builder()
                .username("jdoe")
                .roles(List.of("ANALYST", "AUDITOR"))
                .ipAddress("192.154.0.1")
                .loginTimestamp(Instant.parse("2020-08-16T12:25:14Z").toEpochMilli())
                .build(),
            UserAuthActivityEventDto.builder()
                .username("asmith")
                .roles(List.of("BUSINESS_OPERATOR"))
                .ipAddress("192.122.0.8")
                .loginTimestamp(Instant.parse("2020-08-16T12:35:27Z").toEpochMilli())
                .build(),
            UserAuthActivityEventDto.builder()
                .username("asmith")
                .roles(List.of("BUSINESS_OPERATOR"))
                .ipAddress("192.122.0.8")
                .logoutTimestamp(Instant.parse("2020-08-16T12:50:38Z").toEpochMilli())
                .build()));

    underTest.generate();

    verify(fileLineWriter).write(
        eq(REPORTS_DIR),
        anyString(),
        argThat(streamThat(hasItems(
            "Access_ID,Access_Profile,Access_Country,Access_SourceIP,"
                + "Access_LoginTimeStamp,Access_LogoutTimeStamp",
            "jdoe,\"ANALYST,AUDITOR\",Global,192.154.0.1,16082020 12:25:14,",
            "asmith,BUSINESS_OPERATOR,Global,192.122.0.8,16082020 12:35:27,",
            "asmith,BUSINESS_OPERATOR,Global,192.122.0.8,,16082020 12:50:38"))));
  }

  @Test
  void queriesForEventsInTheDateRangeFromProvider() throws IOException {
    OffsetDateTime from = OffsetDateTime.now().minusHours(6);
    OffsetDateTime to = OffsetDateTime.now();
    when(dateRangeProvider.latestDateRange()).thenReturn(new DateRange(from, to));

    underTest.generate();

    verify(userAuthActivityEventProvider).provide(from, to);
  }

  @Test
  void storesReportInTheSpecifiedFolderUnderNameSuffixedByTimestamp() throws IOException {
    underTest = new UserAuthActivityReportGenerator(
        dateRangeProvider,
        userAuthActivityEventProvider,
        REPORTS_DIR,
        fileLineWriter,
        new MockTimeSource(Instant.parse("2020-08-16T12:25:30Z")));

    underTest.generate();

    verify(fileLineWriter).write(
        eq(REPORTS_DIR),
        eq("SURVILLANCE_OPTIMIZATION_Login_Logout_20200816122530.csv"),
        any(Stream.class));
  }
}
