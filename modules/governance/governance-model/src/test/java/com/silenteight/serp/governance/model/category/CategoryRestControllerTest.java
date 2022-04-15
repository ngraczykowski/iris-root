package com.silenteight.serp.governance.model.category;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.model.category.CategoryFixture.APTYPE_CATEGORY;
import static com.silenteight.serp.governance.model.category.CategoryFixture.APTYPE_CATEGORY_NAME;
import static com.silenteight.serp.governance.model.category.CategoryFixture.APTYPE_VALUES;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import(CategoryRestController.class)
class CategoryRestControllerTest extends BaseRestControllerTest {

  private static final String CATEGORIES_URL = "/v1/categories";

  @MockBean
  private CategoryRegistry registry;

  @TestWithRole(roles = { APPROVER, MODEL_TUNER, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its200_whenInvoked() {
    given(registry.getAllCategories()).willReturn(List.of(APTYPE_CATEGORY));

    get(CATEGORIES_URL)
        .statusCode(OK.value())
        .body("categories[0].name", is(APTYPE_CATEGORY_NAME))
        .body("categories[0].values", is(APTYPE_VALUES));
  }

  @TestWithRole(roles = { USER_ADMINISTRATOR })
  void its403_whenNotPermittedRole() {
    get(CATEGORIES_URL).statusCode(FORBIDDEN.value());
  }
}
