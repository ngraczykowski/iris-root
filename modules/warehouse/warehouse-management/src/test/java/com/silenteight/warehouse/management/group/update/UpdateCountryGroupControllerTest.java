package com.silenteight.warehouse.management.group.update;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.warehouse.management.group.domain.CountryGroupService;
import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;
import com.silenteight.warehouse.management.group.update.dto.UpdateCountryGroupRequest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.opendistro.roles.RolesFixtures.COUNTRY_GROUP_ID;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.COUNTRY_GROUP_URL;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.NAME;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Import({ UpdateCountryGroupController.class, UpdateCountryGroupConfiguration.class })
class UpdateCountryGroupControllerTest extends BaseRestControllerTest {

  private static final String UPDATE_COUNTRY_GROUP_URL = COUNTRY_GROUP_URL + "/" + COUNTRY_GROUP_ID;

  @MockBean
  private CountryGroupService countryGroupService;

  @TestWithRole(roles = { USER_ADMINISTRATOR })
  void its204_whenCountryGroupUpdated() {
    UpdateCountryGroupRequest dto = new UpdateCountryGroupRequest(NAME);

    patch(UPDATE_COUNTRY_GROUP_URL, dto)
        .contentType(anything())
        .statusCode(NO_CONTENT.value());

    ArgumentCaptor<UpdateCountryGroupRequest> captor =
        ArgumentCaptor.forClass(UpdateCountryGroupRequest.class);
    verify(countryGroupService).update(any(), captor.capture());
    assertThat(captor.getValue().getName()).isEqualTo(NAME);
  }

  @TestWithRole(roles = { MODEL_TUNER, APPROVER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    UpdateCountryGroupRequest dto = new UpdateCountryGroupRequest(NAME);

    patch(UPDATE_COUNTRY_GROUP_URL, dto)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  @ParameterizedTest
  @MethodSource("com.silenteight.warehouse.management.group.CountryGroupFixtures#getForbiddenCharsAsInput")
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its400_whenUpdateCountryGroupRequestContainsForbiddenCharacters(String name) {
    UpdateCountryGroupRequest dto = new UpdateCountryGroupRequest(name);

    patch(UPDATE_COUNTRY_GROUP_URL, dto)
        .contentType(anything())
        .statusCode(BAD_REQUEST.value());
  }

  @ParameterizedTest
  @MethodSource("com.silenteight.warehouse.management.group.CountryGroupFixtures#getAllowedCharsAsInput")
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its201_whenUpdateCountryGroupRequestContainsAllowedCharacters(String name) {
    UpdateCountryGroupRequest dto = new UpdateCountryGroupRequest(name);

    patch(UPDATE_COUNTRY_GROUP_URL, dto)
        .contentType(anything())
        .statusCode(NO_CONTENT.value());
  }
}
