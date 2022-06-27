package com.silenteight.sens.webapp.audit.report;

import com.silenteight.sens.webapp.report.ReportProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sens.webapp.reports.audit")
class AuditReportProperties extends ReportProperties {
}
