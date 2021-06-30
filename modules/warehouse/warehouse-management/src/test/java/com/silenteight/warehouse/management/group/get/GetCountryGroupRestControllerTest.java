package com.silenteight.warehouse.management.group.get;


import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.COUNTRY_GROUP_DTO;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.COUNTRY_GROUP_URL;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.NAME;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.UUID;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import(GetCountryGroupRestController.class)
class GetCountryGroupRestControllerTest extends BaseRestControllerTest {

  private static final String GET_COUNTRY_GROUP_URL = COUNTRY_GROUP_URL + "/" + UUID;

  @MockBean
  private GetCountryGroupQuery countryGroupQuery;

  @TestWithRole(roles = { USER_ADMINISTRATOR })
  void its200_whenCountryGroupFound() {
    when(countryGroupQuery.get(UUID)).thenReturn(COUNTRY_GROUP_DTO);

    get(GET_COUNTRY_GROUP_URL)
        .statusCode(OK.value())
        .body("id", is(UUID.toString()))
        .body("name", is(NAME));
  }

  @TestWithRole(roles = { MODEL_TUNER, APPROVER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    get(GET_COUNTRY_GROUP_URL).statusCode(FORBIDDEN.value());
  }
}
