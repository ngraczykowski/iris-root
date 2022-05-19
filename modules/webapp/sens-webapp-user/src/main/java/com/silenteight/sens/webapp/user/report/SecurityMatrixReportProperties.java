package com.silenteight.sens.webapp.user.report;

import com.silenteight.sens.webapp.report.ReportProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sens.webapp.reports.security-matrix")
class SecurityMatrixReportProperties extends ReportProperties {
}