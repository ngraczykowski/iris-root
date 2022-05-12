package com.silenteight.warehouse.management.group.list;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.warehouse.common.opendistro.roles.RolesFixtures.COUNTRY_GROUP_ID;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.COUNTRY_GROUP_DTO;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.COUNTRY_GROUP_URL;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.NAME;
import static java.util.List.of;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import(ListCountryGroupController.class)
class ListCountryGroupControllerTest extends BaseRestControllerTest {

  @MockBean
  private ListCountryGroupQuery countryGroupQuery;

  @TestWithRole(roles = { USER_ADMINISTRATOR })
  void its200_whenCountryGroupFound() {
    when(countryGroupQuery.listAll()).thenReturn(of(COUNTRY_GROUP_DTO));

    get(COUNTRY_GROUP_URL)
        .statusCode(OK.value())
        .body("size()", is(1))
        .body("[0].id", is(COUNTRY_GROUP_ID.toString()))
        .body("[0].name", is(NAME));
  }

  @TestWithRole(roles = { MODEL_TUNER, APPROVER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    get(COUNTRY_GROUP_URL).statusCode(FORBIDDEN.value());
  }
}
