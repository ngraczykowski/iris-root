package com.silenteight.sens.webapp.report;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;

import java.util.Map;

@RequiredArgsConstructor
class ReportGeneratorFacade {

  private static final String SUCCESS_STATUS = "SUCCESS";
  private static final String FAILED_STATUS = "FAILED";

  @NonNull
  private final ReportProvider reportProvider;

  @NonNull
  private final AuditTracer auditTracer;

  Report generate(String reportName, String ipAddress, Map<String, String> parameters) {
    try {
      ReportGenerator reportGenerator = reportProvider.getReportGenerator(reportName);
      Report report = reportGenerator.generateReport(parameters);
      auditTracer.save(
          new ReportGeneratedEvent(
              reportName,
              Report.class.getName(),
              createSuccessDetails(ipAddress, parameters)));

      return report;
    } catch (Exception e) {
      auditTracer.save(
          new ReportGeneratedEvent(
              reportName,
              Report.class.getName(),
              createFailedDetails(ipAddress, parameters)));

      throw e;
    }
  }

  private static ReportGenerationDetails createSuccessDetails(
      String ipAddress, Map<String, String> parameters) {

    return createDetails(SUCCESS_STATUS, ipAddress, parameters);
  }

  private static ReportGenerationDetails createFailedDetails(
      String ipAddress, Map<String, String> parameters) {

    return createDetails(FAILED_STATUS, ipAddress, parameters);
  }

  private static ReportGenerationDetails createDetails(
      String status, String ipAddress, Map<String, String> parameters) {

    return ReportGenerationDetails.builder()
        .status(status)
        .ipAddress(ipAddress)
        .parameters(parameters)
        .build();
  }
}
