package com.silenteight.warehouse.management.country.get;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.warehouse.management.country.CountryService;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.management.country.CountriesFixtures.COUNTRIES;
import static com.silenteight.warehouse.management.country.CountriesFixtures.UUID;
import static com.silenteight.warehouse.management.country.get.GetCountriesRestController.COUNTRIES_URL;
import static com.silenteight.warehouse.management.country.get.GetCountriesRestController.COUNTRY_GROUP_ID_PARAM;
import static java.util.Map.of;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import(GetCountriesRestController.class)
class GetCountriesRestControllerTest extends BaseRestControllerTest {

  private static final String TEST_GET_COUNTRIES_URL = fromUriString(COUNTRIES_URL)
      .build(of(COUNTRY_GROUP_ID_PARAM, UUID))
      .toString();

  @MockBean
  private CountryService countryService;

  @TestWithRole(roles = { USER_ADMINISTRATOR })
  void its200_whenCountriesFound() {
    when(countryService.getCountries(UUID)).thenReturn(COUNTRIES);

    get(TEST_GET_COUNTRIES_URL)
        .statusCode(OK.value())
        .body("$", is(COUNTRIES));
  }

  @TestWithRole(roles = { MODEL_TUNER, APPROVER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    get(TEST_GET_COUNTRIES_URL).statusCode(FORBIDDEN.value());
  }
}
