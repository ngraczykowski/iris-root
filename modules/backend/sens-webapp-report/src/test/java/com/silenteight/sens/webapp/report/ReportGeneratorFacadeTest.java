package com.silenteight.sens.webapp.report;

import com.silenteight.sens.webapp.audit.api.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.api.trace.AuditEvent;
import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.common.support.csv.LinesSupplier;
import com.silenteight.sens.webapp.common.support.csv.SimpleLinesSupplier;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEvent.EntityAction.CREATE;
import static com.silenteight.sens.webapp.report.ReportTestFixtures.REPORT_CONTENT;
import static com.silenteight.sens.webapp.report.ReportTestFixtures.REPORT_NAME;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportGeneratorFacadeTest {

  private static final String IP_ADDRESS = "192.134.0.17";
  private static final String SUCCESS_STATUS = "SUCCESS";
  private static final String FAILED_STATUS = "FAILED";

  @InjectMocks
  private ReportGeneratorFacade underTest;

  @Mock
  private ReportProvider reportProvider;

  @Mock
  private AuditTracer auditTracer;

  @Test
  void reportGenerated_generateReportAndSaveAuditEvent() {
    // given
    UUID correlationId = RequestCorrelation.id();
    Map<String, String> parameters = Map.of("param1", "value1", "param2", "value2");
    when(reportProvider.getReportGenerator(REPORT_NAME)).thenReturn(new DummyReportGenerator());

    // when
    Report report = underTest.generate(REPORT_NAME, IP_ADDRESS, parameters);

    // then
    assertThat(report.getReportFileName()).isEqualTo(REPORT_NAME);
    assertThat(report.getReportContent().lines()).isEqualTo(REPORT_CONTENT);

    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer).save(eventCaptor.capture());
    AuditEvent auditEvent = eventCaptor.getValue();

    assertAuditEvent(auditEvent, correlationId);
    assertThat(auditEvent.getDetails()).isEqualTo(successDetails(parameters));
  }

  @Test
  void exceptionThrown_rethrowExceptionAndSaveAuditEvent() {
    // given
    UUID correlationId = RequestCorrelation.id();
    Map<String, String> parameters = Map.of("param1", "value1", "param2", "value2");
    when(reportProvider.getReportGenerator(REPORT_NAME))
        .thenReturn(new ThrowExceptionReportGenerator());

    // when
    ThrowingCallable generateReportCall =
        () -> underTest.generate(REPORT_NAME, IP_ADDRESS, parameters);

    // then
    assertThatThrownBy(generateReportCall).isInstanceOf(ReportGenerationException.class);

    ArgumentCaptor<AuditEvent> eventCaptor = ArgumentCaptor.forClass(AuditEvent.class);
    verify(auditTracer).save(eventCaptor.capture());
    AuditEvent auditEvent = eventCaptor.getValue();

    assertAuditEvent(auditEvent, correlationId);
    assertThat(auditEvent.getDetails()).isEqualTo(failedDetails(parameters));
  }

  private static void assertAuditEvent(AuditEvent auditEvent, UUID correlationId) {
    assertThat(auditEvent.getType()).isEqualTo("ReportGenerated");
    assertThat(auditEvent.getEntityId()).isEqualTo(REPORT_NAME);
    assertThat(auditEvent.getEntityClass()).isEqualTo(Report.class.getName());
    assertThat(auditEvent.getEntityAction()).isEqualTo(CREATE.toString());
    assertThat(auditEvent.getCorrelationId()).isEqualTo(correlationId);
    assertThat(auditEvent.getDetails()).isInstanceOf(ReportGenerationDetails.class);
  }

  private static ReportGenerationDetails successDetails(Map<String, String> parameters) {
    return details(SUCCESS_STATUS, parameters);
  }

  private static ReportGenerationDetails failedDetails(Map<String, String> parameters) {
    return details(FAILED_STATUS, parameters);
  }

  private static ReportGenerationDetails details(String status, Map<String, String> parameters) {
    return ReportGenerationDetails.builder()
        .status(status)
        .ipAddress(IP_ADDRESS)
        .parameters(parameters)
        .build();
  }

  private static class DummyReportGenerator implements ReportGenerator {

    @Override
    public String getName() {
      return REPORT_NAME;
    }

    @Override
    public Report generateReport(Map<String, String> parameters) {
      return new DummyReport();
    }
  }

  private static class DummyReport implements Report {

    @Override
    public String getReportFileName() {
      return REPORT_NAME;
    }

    @Override
    public LinesSupplier getReportContent() {
      return new SimpleLinesSupplier(REPORT_CONTENT);
    }
  }

  private static class ThrowExceptionReportGenerator implements ReportGenerator {

    @Override
    public String getName() {
      return REPORT_NAME;
    }

    @Override
    public Report generateReport(Map<String, String> parameters) {
      throw new ReportGenerationException();
    }
  }

  private static class ReportGenerationException extends RuntimeException {

    private static final long serialVersionUID = 1424521780948919520L;
  }
}
