package com.silenteight.sens.webapp.user.report;

import com.silenteight.sens.webapp.user.config.RolesProperties;
import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.base.common.time.DigitsOnlyDateFormatter;
import com.silenteight.sep.base.common.time.IsoOffsetDateFormatter;
import com.silenteight.sep.usermanagement.api.user.UserQuery;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    UsersReportProperties.class, SecurityMatrixReportProperties.class })
class UsersReportConfiguration {

  @Bean
  UsersReportGenerator usersReportGenerator(
      UserQuery userQuery, RolesProperties rolesProperties,
      UsersReportProperties usersReportProperties) {

    return new UsersReportGenerator(
        DefaultTimeSource.INSTANCE,
        DigitsOnlyDateFormatter.INSTANCE,
        IsoOffsetDateFormatter.INSTANCE,
        userQuery,
        rolesProperties.getRolesScope(),
        usersReportProperties);
  }

  @Bean
  SecurityMatrixReportGenerator securityMatrixReportGenerator(
      SecurityMatrixReportProperties reportProperties) {

    return new SecurityMatrixReportGenerator(reportProperties);
  }
}
