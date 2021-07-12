package com.silenteight.warehouse.management.group.create;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.warehouse.common.opendistro.roles.RolesFixtures.COUNTRY_GROUP_ID;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.COUNTRY_GROUP_URL;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.NAME;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import(CreateCountryGroupRestController.class)
class CreateCountryGroupRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private CreateCountryGroupUseCase createCountryGroupUseCase;

  @TestWithRole(roles = { USER_ADMINISTRATOR })
  void its201_whenCountryGroupCreated() {
    CountryGroupDto dto = new CountryGroupDto(COUNTRY_GROUP_ID, NAME);

    post(COUNTRY_GROUP_URL, dto)
        .contentType(anything())
        .statusCode(CREATED.value());

    verify(createCountryGroupUseCase).activate(dto);
  }

  @TestWithRole(roles = { MODEL_TUNER, APPROVER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    CountryGroupDto dto = new CountryGroupDto(COUNTRY_GROUP_ID, NAME);

    post(COUNTRY_GROUP_URL, dto)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
