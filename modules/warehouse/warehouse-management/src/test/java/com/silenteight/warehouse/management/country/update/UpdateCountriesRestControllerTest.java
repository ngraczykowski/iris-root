package com.silenteight.warehouse.management.country.update;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.warehouse.management.country.CountryService;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.management.country.CountriesFixtures.COUNTRIES;
import static com.silenteight.warehouse.management.country.CountriesFixtures.UUID;
import static com.silenteight.warehouse.management.country.update.UpdateCountriesRestController.COUNTRIES_URL;
import static com.silenteight.warehouse.management.country.update.UpdateCountriesRestController.COUNTRY_GROUP_ID_PARAM;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import(UpdateCountriesRestController.class)
class UpdateCountriesRestControllerTest extends BaseRestControllerTest {

  private static final String TEST_UPDATE_COUNTRIES_URL = fromUriString(COUNTRIES_URL)
      .build(of(COUNTRY_GROUP_ID_PARAM, UUID))
      .toString();

  @MockBean
  private CountryService countryService;

  @TestWithRole(roles = { USER_ADMINISTRATOR })
  void its200_whenCountriesFound() {
    put(TEST_UPDATE_COUNTRIES_URL, COUNTRIES).statusCode(NO_CONTENT.value());
  }

  @TestWithRole(roles = { MODEL_TUNER, APPROVER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    put(TEST_UPDATE_COUNTRIES_URL, COUNTRIES).statusCode(FORBIDDEN.value());
  }
}

