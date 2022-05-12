package com.silenteight.warehouse.management.group.delete;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.warehouse.common.opendistro.roles.RolesFixtures.COUNTRY_GROUP_ID;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.COUNTRY_GROUP_DELETE_URL;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Import(DeleteCountryGroupRestController.class)
class DeleteCountryGroupRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private DeleteCountryGroupUseCase useCase;

  @TestWithRole(roles = { USER_ADMINISTRATOR })
  void its200_whenCountryGroupDeleted() {
    delete(COUNTRY_GROUP_DELETE_URL)
        .contentType(anything())
        .statusCode(NO_CONTENT.value());

    verify(useCase).activate(COUNTRY_GROUP_ID);
  }

  @TestWithRole(roles = { MODEL_TUNER, APPROVER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    delete(COUNTRY_GROUP_DELETE_URL)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}