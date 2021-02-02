package com.silenteight.serp.governance.policy.featurevector;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorDto;
import com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorsDto;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static java.util.Arrays.asList;
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
  private static final List<String> COLUMNS = List.of("nameAgent", "dateAgent", "documentAgent");
  private static final String SIGNATURE_1 = "signature_1";
  private static final List<String> FEATURE_VALUES_1 = asList("PERFECT_MATCH", null, "MATCH");
  private static final String SIGNATURE_2 = "signature_2";
  private static final List<String> FEATURE_VALUES_2 = asList("DIGIT_MATCH", "MATCH", null);
  private static final FeatureVectorsDto FEATURE_VECTORS_DTO = FeatureVectorsDto
      .builder()
      .columns(COLUMNS)
      .featureVectors(
          List.of(
              FeatureVectorDto.builder()
                  .signature(SIGNATURE_1)
                  .values(FEATURE_VALUES_1)
                  .build(),
              FeatureVectorDto.builder()
                  .signature(SIGNATURE_2)
                  .values(FEATURE_VALUES_2)
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
        .body("columns", is(COLUMNS))
        .body("featureVectors[0].signature", is(SIGNATURE_1))
        .body("featureVectors[0].values", is(FEATURE_VALUES_1))
        .body("featureVectors[1].signature", is(SIGNATURE_2))
        .body("featureVectors[1].values", is(FEATURE_VALUES_2));
  }
}
