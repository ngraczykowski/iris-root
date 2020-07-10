package com.silenteight.sens.webapp.backend.reasoningbranch.feature.value;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.backend.reasoningbranch.feature.value.ReasoningBranchFeatureValueRestControllerTest.ReasoningBranchFeatureValueRestControllerFixtures.FEATURE_VALUE_1;
import static com.silenteight.sens.webapp.backend.reasoningbranch.feature.value.ReasoningBranchFeatureValueRestControllerTest.ReasoningBranchFeatureValueRestControllerFixtures.FEATURE_VALUE_2;
import static com.silenteight.sens.webapp.backend.reasoningbranch.feature.value.ReasoningBranchFeatureValueRestControllerTest.ReasoningBranchFeatureValueRestControllerFixtures.FEATURE_VECTOR_ID;
import static com.silenteight.sens.webapp.backend.reasoningbranch.feature.value.ReasoningBranchFeatureValueRestControllerTest.ReasoningBranchFeatureValueRestControllerFixtures.TREE_ID;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ ReasoningBranchFeatureValueRestController.class })
class ReasoningBranchFeatureValueRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private FeatureValuesQuery featureValuesQuery;

  @TestWithRole(roles = { APPROVER, BUSINESS_OPERATOR })
  void its200WithCorrectBody_whenFound() {
    given(featureValuesQuery.findFeatureValues(TREE_ID, FEATURE_VECTOR_ID))
        .willReturn(asList(FEATURE_VALUE_1, FEATURE_VALUE_2));

    get(mappingForFeatureValues(TREE_ID, FEATURE_VECTOR_ID))
        .statusCode(OK.value())
        .body("size()", is(2))
        .body("[0]", equalTo(FEATURE_VALUE_1))
        .body("[1]", equalTo(FEATURE_VALUE_2));
  }

  @TestWithRole(roles = { ADMIN, ANALYST, AUDITOR })
  void its403_whenNotPermittedRole() {
    get(mappingForFeatureValues(TREE_ID, FEATURE_VECTOR_ID))
        .statusCode(FORBIDDEN.value());
  }

  private String mappingForFeatureValues(long decisionTreeId, long featureVectorId) {
    return format("/reasoning-branches/features/%d-%d/values", decisionTreeId, featureVectorId);
  }

  static class ReasoningBranchFeatureValueRestControllerFixtures {

    static final long TREE_ID = 2L;
    static final long FEATURE_VECTOR_ID = 5L;
    static final String FEATURE_VALUE_1 = "feature-value-1";
    static final String FEATURE_VALUE_2 = "feature-value-2";
  }
}
