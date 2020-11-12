package com.silenteight.sens.webapp.backend.external.apps;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.http.HttpStatus.OK;


@EnableConfigurationProperties(ExternalAppsProperties.class)
@Import({  ExternalAppsConfiguration.class, ExternalAppsController.class })
class NoExternalAppsControllerTest extends BaseRestControllerTest {

  @TestWithRole(roles = { ADMINISTRATOR, ANALYST, AUDITOR, APPROVER, BUSINESS_OPERATOR })
  void its200_whenListingApps() {
    get("/apps/list").statusCode(OK.value()).body("apps", equalTo(emptyList()));
  }
}
