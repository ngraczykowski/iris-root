package com.silenteight.sens.webapp.scb.report;

import com.silenteight.sens.webapp.common.support.csv.FileLineWriter;
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
import static java.io.File.separator;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditHistoryReportGeneratorTest {

  private static final DateRange DEFAULT_DATE_RANGE = new DateRange(
      OffsetDateTime.now().minusHours(1), OffsetDateTime.now());

  private static final String REPORTS_DIR = "path/to/reports";

  @Mock
  private DateRangeProvider dateRangeProvider;

  @Mock
  private AuditHistoryEventProvider auditHistoryEventProvider;

  @Mock
  private FileLineWriter fileLineWriter;

  private AuditHistoryReportGenerator underTest;

  @BeforeEach
  void setUp() {
    underTest = new AuditHistoryReportGenerator(
        dateRangeProvider,
        auditHistoryEventProvider,
        REPORTS_DIR,
        fileLineWriter,
        new MockTimeSource(Instant.now()));

    when(dateRangeProvider.latestDateRange()).thenReturn(DEFAULT_DATE_RANGE);
  }

  @Test
  void generatesReportRowsFromEvents() throws IOException {
    when(auditHistoryEventProvider.provide(any(OffsetDateTime.class), any(OffsetDateTime.class)))
        .thenReturn(List.of(
            AuditHistoryEventDto.builder()
                .userName("userA")
                .status("SUCCESS")
                .ipAddress("192.122.0.8")
                .timestamp(Instant.parse("2020-08-15T12:14:32Z"))
                .build(),
            AuditHistoryEventDto.builder()
                .userName("userB")
                .status("FAILED")
                .ipAddress("192.154.0.1")
                .timestamp(Instant.parse("2020-09-20T13:15:48Z"))
                .build()));

    underTest.generate();

    verify(fileLineWriter).write(
        anyString(),
        argThat(streamThat(hasItems(
            "\"Audit_ID,Audit_Status,Access_SourceIP,Audit_Country,Audit_LoginTimeStamp\"",
            "\"userA,SUCCESS,192.122.0.8,Global,\"\"15082020 12:14:32\"\"\"",
            "\"userB,FAILED,192.154.0.1,Global,\"\"20092020 13:15:48\"\"\""))));
  }

  @Test
  void queriesForEventsInTheDateRangeFromProvider() throws IOException {
    OffsetDateTime from = OffsetDateTime.now().minusHours(6);
    OffsetDateTime to = OffsetDateTime.now();
    when(dateRangeProvider.latestDateRange()).thenReturn(new DateRange(from, to));

    underTest.generate();

    verify(auditHistoryEventProvider).provide(from, to);
  }

  @Test
  void storesReportInTheSpecifiedFolderUnderNameSuffixedByTimestamp() throws IOException {
    underTest = new AuditHistoryReportGenerator(
        dateRangeProvider,
        auditHistoryEventProvider,
        REPORTS_DIR,
        fileLineWriter,
        new MockTimeSource(Instant.parse("2020-05-22T15:15:30Z")));

    underTest.generate();

    verify(fileLineWriter).write(
        eq(REPORTS_DIR
            + separator
            + "SURVILLANCE_OPTIMIZATION_AuditHistory_20200522151530.csv"),
        any(Stream.class));
  }
}
