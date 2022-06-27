package com.silenteight.sens.webapp.scb.report;

import com.silenteight.auditing.bs.AuditingFinder;
import com.silenteight.sens.webapp.backend.report.domain.ReportMetadataService;
import com.silenteight.sens.webapp.common.support.file.FileLineWriter;
import com.silenteight.sens.webapp.user.config.RolesProperties;
import com.silenteight.sens.webapp.user.roles.UserRolesRetriever;
import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.usermanagement.api.configuration.ConfigurationQuery;
import com.silenteight.sep.usermanagement.api.event.EventQuery;
import com.silenteight.sep.usermanagement.api.user.UserQuery;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.validation.Valid;

@Configuration
@EnableScheduling
@EnableConfigurationProperties({
    ScbReportsProperties.class, AccountsReportProperties.class, EntitlementReportProperties.class })
class ScbReportsConfiguration {

  @Bean
  AccountsReportGenerator accountsReportGenerator(
      UserQuery userQuery,
      @Valid RolesProperties rolesProperties,
      @Valid AccountsReportProperties reportProperties) {

    return new AccountsReportGenerator(
        userQuery,
        DefaultTimeSource.INSTANCE,
        new ScbReportDateFormatter(),
        rolesProperties.getRolesScope(),
        reportProperties);
  }

  @Bean
  EntitlementReportGenerator entitlementReportGenerator(
      @Valid EntitlementReportProperties reportProperties) {
    return new EntitlementReportGenerator(
        DefaultTimeSource.INSTANCE, new ScbReportDateFormatter(), reportProperties);
  }

  @Bean
  IdManagementReportGenerator idManagementReportGenerator(
      IdManagementEventProvider idManagementEventProvider,
      @Valid ScbReportsProperties scbReportsProperties) {

    return new IdManagementReportGenerator(
        new DateRangeProvider(
            scbReportsProperties.idManagementCronExpression(), DefaultTimeSource.INSTANCE),
        idManagementEventProvider,
        scbReportsProperties.getDir(),
        new FileLineWriter(),
        DefaultTimeSource.INSTANCE);
  }

  @Bean
  IdManagementEventProvider idManagementEventProvider(AuditingFinder auditingFinder) {
    return new IdManagementEventProvider(auditingFinder);
  }

  @Bean
  AuditHistoryReportGenerator auditHistoryReportGenerator(
      AuditHistoryEventProvider auditHistoryEventProvider,
      @Valid ScbReportsProperties scbReportsProperties) {

    return new AuditHistoryReportGenerator(
        new DateRangeProvider(
            scbReportsProperties.auditHistoryCronExpression(), DefaultTimeSource.INSTANCE),
        auditHistoryEventProvider,
        scbReportsProperties.getDir(),
        new FileLineWriter(),
        DefaultTimeSource.INSTANCE);
  }

  @Bean
  AuditHistoryEventProvider auditHistoryEventProvider(EventQuery eventQuery) {
    return new AuditHistoryEventProvider(eventQuery);
  }

  @Bean
  UserAuthActivityReportGenerator userAuthActivityReportGenerator(
      UserAuthActivityEventProvider dataProvider,
      @Valid ScbReportsProperties properties) {

    return new UserAuthActivityReportGenerator(
        new DateRangeProvider(
            properties.userAuthActivityCronExpression(), DefaultTimeSource.INSTANCE),
        dataProvider,
        properties.getDir(),
        new FileLineWriter(),
        DefaultTimeSource.INSTANCE);
  }

  @Bean
  UserAuthActivityEventProvider userAuthActivityEventProvider(
      ConfigurationQuery configurationQuery,
      EventQuery eventQuery,
      ReportMetadataService reportMetadataService,
      UserRolesRetriever userRolesRetriever,
      @Valid RolesProperties rolesProperties) {

    return new UserAuthActivityEventProvider(
        configurationQuery,
        eventQuery,
        reportMetadataService,
        userRolesRetriever,
        DefaultTimeSource.INSTANCE.timeZone().toZoneId(),
        rolesProperties.getRolesScope());
  }
}
