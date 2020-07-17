package com.silenteight.sens.webapp.user.report;

import com.silenteight.sens.webapp.user.UserListQuery;
import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.base.common.time.DigitsOnlyDateFormatter;
import com.silenteight.sep.base.common.time.IsoOffsetDateFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UsersReportConfiguration {

  @Bean
  UsersReportGenerator usersReportGenerator(UserListQuery userListQuery) {
    return new UsersReportGenerator(
        DefaultTimeSource.INSTANCE,
        DigitsOnlyDateFormatter.INSTANCE,
        IsoOffsetDateFormatter.INSTANCE,
        userListQuery);
  }
}
