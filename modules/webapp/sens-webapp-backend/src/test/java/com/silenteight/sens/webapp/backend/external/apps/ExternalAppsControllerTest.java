package com.silenteight.sens.webapp.backend.external.apps;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.FOUND;


@EnableConfigurationProperties(ExternalAppsProperties.class)
@Import({  ExternalAppsConfiguration.class, ExternalAppsController.class })
@TestPropertySource(properties = "sens.webapp.external.apps.reportingUrl=" +
    ExternalAppsControllerTest.REDIRECT_URL)
class ExternalAppsControllerTest extends BaseRestControllerTest {

  static final String REDIRECT_URL = "http://localhost/reporting/ui";

  @TestWithRole(roles = { AUDITOR, BUSINESS_OPERATOR })
  void its307_whenProperRole() {
    get("/apps/reporting").statusCode(FOUND.value()).header("Location", REDIRECT_URL);
  }

  @TestWithRole(roles = { ADMINISTRATOR, ANALYST, APPROVER })
  void its403_whenNotPermittedRole() {
    get("/apps/reporting").statusCode(FORBIDDEN.value());
  }
}
