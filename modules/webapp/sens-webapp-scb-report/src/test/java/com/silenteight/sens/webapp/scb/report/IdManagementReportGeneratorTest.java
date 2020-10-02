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
import static java.time.Instant.now;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdManagementReportGeneratorTest {

  private static final DateRange DEFAULT_DATE_RANGE =
      new DateRange(OffsetDateTime.now(), OffsetDateTime.now());

  private static final String REPORTS_DIR = "path/to/reports";

  @Mock
  private DateRangeProvider dateRangeProvider;

  @Mock
  private IdManagementEventProvider idManagementEventProvider;

  @Mock
  private FileLineWriter lineWriter;

  private IdManagementReportGenerator idManagementReportGenerator;

  @BeforeEach
  void setUp() {
    when(dateRangeProvider.latestDateRange()).thenReturn(DEFAULT_DATE_RANGE);

    idManagementReportGenerator = new IdManagementReportGenerator(
        dateRangeProvider,
        idManagementEventProvider,
        REPORTS_DIR,
        lineWriter,
        new MockTimeSource(now()));
  }

  @Test
  void generatesReportRowsFromEvents() throws IOException {
    when(idManagementEventProvider
        .idManagementEvents(any(OffsetDateTime.class), any(OffsetDateTime.class)))
        .thenReturn(List.of(
            IdManagementEventDto.builder()
                .username("userA")
                .role("ANALYST")
                .principal("userB")
                .action("CREATE")
                .timestamp(Instant.parse("2020-08-15T12:14:32Z"))
                .build(),
            IdManagementEventDto.builder()
                .username("userABC")
                .role("APPROVER")
                .principal("userBCD")
                .action("DELETE")
                .timestamp(Instant.parse("2020-09-20T13:15:48Z"))
                .build()));

    idManagementReportGenerator.generate();

    verify(lineWriter).write(
        eq(REPORTS_DIR),
        anyString(),
        argThat(streamThat(hasItems(
            "\"AppID_ID,\"\t\"AppID_Name,\"\t\"AppID_Implemented_By,\"\t\""
                + "AppID_Implemented_TimeStamp,\"\t\"AppID_Status,\"\tAppID_Country",
            "\"userA,\"\t\"ANALYST,\"\t\"userB,\"\t\"\"\"15082020  12:14:32,\"\"\"\t\"CREATE,"
                + "\"\tGlobal",
            "\"userABC,\"\t\"APPROVER,\"\t\"userBCD,\"\t\"\"\"20092020  13:15:48,\"\"\"\t\"DELETE,"
                + "\"\tGlobal"))));
  }

  @Test
  void queriesForEventsInTheDateRangeFromProvider() throws IOException {

    OffsetDateTime from = OffsetDateTime.now().minusHours(6);
    OffsetDateTime to = OffsetDateTime.now();
    when(dateRangeProvider.latestDateRange()).thenReturn(new DateRange(from, to));

    idManagementReportGenerator.generate();

    verify(idManagementEventProvider).idManagementEvents(from, to);
  }

  @Test
  void storesReportInTheSpecifiedFolderUnderNameSuffixedByTimestamp() throws IOException {
    idManagementReportGenerator =
        new IdManagementReportGenerator(
            dateRangeProvider,
            idManagementEventProvider,
            REPORTS_DIR,
            lineWriter,
            new MockTimeSource(Instant.parse("2020-05-22T15:15:30Z")));

    idManagementReportGenerator.generate();

    verify(lineWriter).write(
        eq(REPORTS_DIR),
        eq("SURVILLANCE_OPTIMIZATION_ID_Management_20200522151530.csv"),
        any(Stream.class));
  }
}
