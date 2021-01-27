package com.silenteight.serp.governance.policy.featurevector;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorDto;
import com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorsDto;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ FeatureVectorStepRestController.class, GenericExceptionControllerAdvice.class })
class FeatureVectorStepRestControllerTest extends BaseRestControllerTest {

  private static final UUID STEP_ID = randomUUID();
  private static final String POLICY_STEP_FEATURE_VECTORS_URL =
      "/v1/steps/" + STEP_ID + "/vectors";
  private static final FeatureVectorsDto FEATURE_VECTORS_DTO = FeatureVectorsDto
      .builder()
      .featureVectors(
          List.of(
              FeatureVectorDto.builder()
                  .featureValues(Map.of("nameAgent", "PERFECT_MATCH", "dateAgent", "MATCH"))
                  .build(),
              FeatureVectorDto.builder()
                  .featureValues(Map.of("documentAgent", "DIGIT_MATCH", "dateAgent", "MATCH"))
                  .build()))
      .build();

  @MockBean
  private FindMatchingFeatureVectorsUseCase findMatchingFeatureVectorsUseCase;

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    get(POLICY_STEP_FEATURE_VECTORS_URL).statusCode(FORBIDDEN.value());
  }

  @TestWithRole(roles = { POLICY_MANAGER })
  void its200_whenStepSolvesFeatureVectors() {
    given(findMatchingFeatureVectorsUseCase.activate(STEP_ID)).willReturn(FEATURE_VECTORS_DTO);

    get(POLICY_STEP_FEATURE_VECTORS_URL)
        .statusCode(OK.value())
        .body("featureVectors.size()", is(2));
  }
}
