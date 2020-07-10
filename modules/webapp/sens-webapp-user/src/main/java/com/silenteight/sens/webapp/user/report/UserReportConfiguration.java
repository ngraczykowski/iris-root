package com.silenteight.sens.webapp.user.report;

import com.silenteight.sens.webapp.user.UserListQuery;
import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.base.common.time.DigitsOnlyDateFormatter;
import com.silenteight.sep.base.common.time.IsoOffsetDateFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserReportConfiguration {

  @Bean
  UserReportGenerator userReportGenerator(UserListQuery userListQuery) {
    return new UserReportGenerator(
        DefaultTimeSource.INSTANCE,
        DigitsOnlyDateFormatter.INSTANCE,
        IsoOffsetDateFormatter.INSTANCE,
        userListQuery);
  }
}
