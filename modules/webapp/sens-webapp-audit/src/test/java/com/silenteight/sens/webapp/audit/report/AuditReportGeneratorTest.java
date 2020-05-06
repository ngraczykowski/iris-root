package com.silenteight.sens.webapp.audit.report;

import com.silenteight.auditing.bs.AuditingFinder;
import com.silenteight.sens.webapp.common.testing.time.MockTimeSource;
import com.silenteight.sens.webapp.report.Report;
import com.silenteight.sens.webapp.report.exception.IllegalParameterException;

import com.google.common.collect.ImmutableMap;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Map;

import static com.silenteight.sens.webapp.audit.report.AuditDataDtoFixtures.DECISION_TREE_ADD_AUDIT_DATA;
import static com.silenteight.sens.webapp.audit.report.AuditDataDtoFixtures.REASONING_BRANCH_CHANGE_AUDIT_DATA;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditReportGeneratorTest {

  @Mock
  private AuditingFinder auditingFinder;

  private AuditReportGenerator underTest;

  @BeforeEach
  void setUp() {
    underTest = new AuditReportGenerator(
        new MockTimeSource(Instant.parse("2020-04-22T15:15:30Z")),
        new TestDateFormatter(),
        auditingFinder);
  }

  @Test
  void throwsIllegalParameterExceptionWhenNoFrom() {
    ThrowingCallable generateReportCall = () -> underTest.generateReport(emptyMap());

    assertThatThrownBy(generateReportCall)
        .isInstanceOf(IllegalParameterException.class)
        .hasMessage("from not provided");
  }

  @Test
  void throwsIllegalParameterExceptionWhenFromIsNotDateTime() {
    ThrowingCallable generateReportCall = () -> underTest.generateReport(Map.of("from", "abc"));

    assertThatThrownBy(generateReportCall)
        .isInstanceOf(IllegalParameterException.class)
        .hasMessage("from must be datetime");
  }

  @Test
  void throwsIllegalParameterExceptionWhenNoTo() {
    ThrowingCallable generateReportCall =
        () -> underTest.generateReport(Map.of("from", "2020-04-05T10:15:30Z"));

    assertThatThrownBy(generateReportCall)
        .isInstanceOf(IllegalParameterException.class)
        .hasMessage("to not provided");
  }

  @Test
  void throwsIllegalParameterExceptionWhenToIsNotDateTime() {
    ThrowingCallable generateReportCall =
        () -> underTest.generateReport(Map.of("from", "2020-04-05T10:15:30Z", "to", "abc"));

    assertThatThrownBy(generateReportCall)
        .isInstanceOf(IllegalParameterException.class)
        .hasMessage("to must be datetime");
  }

  @Test
  void generateAuditReportWhenRequested() throws IOException {
    // given
    String from = "2020-04-05T10:15:30Z";
    String to = "2020-04-28T10:15:30Z";
    when(auditingFinder.find(OffsetDateTime.parse(from), OffsetDateTime.parse(to))).thenReturn(
        asList(DECISION_TREE_ADD_AUDIT_DATA, REASONING_BRANCH_CHANGE_AUDIT_DATA));
    Map<String, String> parameters = ImmutableMap.of(
        "from", from,
        "to", to);

    // when
    Report report = underTest.generateReport(parameters);

    // then
    assertThat(report.getReportFileName()).isEqualTo("audit-2020-04-22 15-15-30.csv");
    assertThat(getReportBody(report))
        .isEqualTo(getReportAsString("webapp/audit/report/audit.csv"));
  }

  private String getReportBody(Report report) {
    return report.getReportContent().lines().collect(joining(""));
  }

  private String getReportAsString(String expectedReportFile) throws IOException {
    InputStream reportStream = getClass().getClassLoader().getResourceAsStream(expectedReportFile);
    return new String(reportStream.readAllBytes());
  }
}
