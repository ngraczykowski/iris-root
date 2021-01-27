package com.silenteight.serp.governance.policy.featurevector;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorsColumnsDto;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ FeatureVectorColumnRestController.class, GenericExceptionControllerAdvice.class })
class FeatureVectorColumnRestControllerTest extends BaseRestControllerTest {

  private static final String FEATURE_VECTORS_COLUMNS_URL = "/v1/steps/vectors/columns";
  private static final FeatureVectorsColumnsDto FEATURE_VECTORS_COLUMNS_DTO =
      FeatureVectorsColumnsDto
          .builder()
          .columns(List.of("dateAgent", "documentAgent", "nameAgent"))
          .build();

  @MockBean
  private FindFeatureVectorsColumnsUseCase findFeatureVectorsColumnsUseCase;

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    get(FEATURE_VECTORS_COLUMNS_URL).statusCode(FORBIDDEN.value());
  }

  @TestWithRole(roles = { POLICY_MANAGER })
  void its200_whenStepSolvesFeatureVectors() {
    given(findFeatureVectorsColumnsUseCase.activate()).willReturn(FEATURE_VECTORS_COLUMNS_DTO);

    get(FEATURE_VECTORS_COLUMNS_URL)
        .statusCode(OK.value())
        .body("columns", hasItems("dateAgent", "documentAgent", "nameAgent"));
  }
}
