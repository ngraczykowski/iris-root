package com.silenteight.sens.webapp.backend.reasoningbranch.feature.name;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ ReasoningBranchFeatureNameRestController.class })
class ReasoningBranchFeatureNameRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private FeatureNamesQuery featureNamesQuery;

  @TestWithRole(roles = { APPROVER, BUSINESS_OPERATOR })
  void its200WithCorrectBody_whenFound() {
    long featureVectorId = 5L;
    String featureName1 = "featureA";
    String featureName2 = "featureB";
    given(featureNamesQuery.findFeatureNames(featureVectorId))
        .willReturn(List.of(featureName1, featureName2));

    get(mappingForFeatureNames(featureVectorId))
        .statusCode(OK.value())
        .body("size()", is(2))
        .body("[0]", equalTo(featureName1))
        .body("[1]", equalTo(featureName2));
  }

  @TestWithRole(roles = { ADMINISTRATOR, ANALYST, AUDITOR })
  void its403_whenNotPermittedRole() {
    get(mappingForFeatureNames(5L)).statusCode(FORBIDDEN.value());
  }

  private static String mappingForFeatureNames(long featureVectorId) {
    return format("/reasoning-branches/features/%d/names", featureVectorId);
  }
}
