package com.silenteight.warehouse.management.group.update;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.warehouse.management.group.domain.CountryGroupService;
import com.silenteight.warehouse.management.group.update.dto.UpdateCountryGroupRequest;

import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.COUNTRY_GROUP_URL;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.NAME;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.UUID;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Import({ UpdateCountryGroupController.class, UpdateCountryGroupConfiguration.class })
class UpdateCountryGroupControllerTest extends BaseRestControllerTest {

  private static final String UPDATE_COUNTRY_GROUP_URL = COUNTRY_GROUP_URL + "/" + UUID;

  @MockBean
  private CountryGroupService countryGroupService;

  @TestWithRole(roles = { BUSINESS_OPERATOR })
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

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRole() {
    UpdateCountryGroupRequest dto = new UpdateCountryGroupRequest(NAME);

    patch(UPDATE_COUNTRY_GROUP_URL, dto)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}