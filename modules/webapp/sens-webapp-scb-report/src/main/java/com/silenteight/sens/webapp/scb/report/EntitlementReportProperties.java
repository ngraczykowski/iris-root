package com.silenteight.sens.webapp.scb.report;

import com.silenteight.sens.webapp.report.ReportProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sens.webapp.reports.entitlement")
class EntitlementReportProperties extends ReportProperties {

}
