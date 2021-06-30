package com.silenteight.serp.governance.vector.list;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.common.web.rest.Paging;
import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorsDto;
import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorsDto.FeatureVectorDto;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ ListVectorsRestController.class, GenericExceptionControllerAdvice.class })
class ListVectorsRestControllerTest extends BaseRestControllerTest {

  private static final String HEADER_TOTAL_COUNT = "X-Total-Count";

  private static final UUID STEP_ID = randomUUID();
  private static final String STEP_NAME = "steps/" + STEP_ID;

  private static final int PAGE_INDEX = 3;
  private static final int PAGE_SIZE = 10;
  private static final String PAGING = format("pageIndex=%d&pageSize=%d", PAGE_INDEX, PAGE_SIZE);
  private static final String LIST_VECTORS_URL = "/v1/vectors";
  private static final String LIST_VECTORS_PAGING_URL = LIST_VECTORS_URL + "?" + PAGING;
  private static final String POLICY_STEP_FEATURE_VECTORS_PAGING_URL =
      format("%s?stepName=%s&%s", LIST_VECTORS_URL, STEP_NAME, PAGING);

  private static final List<String> COLUMNS = List.of("nameAgent", "dateAgent", "documentAgent");
  private static final String SIGNATURE_1 = "signature_1";
  private static final List<String> FEATURE_VALUES_1 = asList("PERFECT_MATCH", null, "MATCH");
  private static final String SIGNATURE_2 = "signature_2";
  private static final List<String> FEATURE_VALUES_2 = asList("DIGIT_MATCH", "MATCH", null);
  private static final List<FeatureVectorDto> FEATURE_VECTORS = List.of(
      FeatureVectorDto.builder().signature(SIGNATURE_1).values(FEATURE_VALUES_1).build(),
      FeatureVectorDto.builder().signature(SIGNATURE_2).values(FEATURE_VALUES_2).build());
  private static final FeatureVectorsDto FEATURE_VECTORS_DTO = FeatureVectorsDto.builder()
      .columns(COLUMNS)
      .featureVectors(FEATURE_VECTORS)
      .build();

  @MockBean
  private FindFeatureVectorsSolvedByStepUseCase findVectorsByStepUseCase;

  @MockBean
  private FindFeatureVectorsSolvedByDefaultPolicyUseCase findVectorsByPolicyUseCase;

  @MockBean
  private ListVectorsQuery listVectorsQuery;

  @TestWithRole(roles = { USER_ADMINISTRATOR, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    get(POLICY_STEP_FEATURE_VECTORS_PAGING_URL).statusCode(FORBIDDEN.value());
  }

  @TestWithRole(roles = { APPROVER, MODEL_TUNER, AUDITOR, QA })
  void its200_whenStepSolvesFeatureVectorsWithPaging() {
    given(findVectorsByStepUseCase.activate(STEP_NAME, new Paging(PAGE_INDEX, PAGE_SIZE)))
        .willReturn(FEATURE_VECTORS_DTO);

    get(POLICY_STEP_FEATURE_VECTORS_PAGING_URL)
        .statusCode(OK.value())
        .body("columns", is(COLUMNS))
        .body("featureVectors[0].signature", is(SIGNATURE_1))
        .body("featureVectors[0].values", is(FEATURE_VALUES_1))
        .body("featureVectors[1].signature", is(SIGNATURE_2))
        .body("featureVectors[1].values", is(FEATURE_VALUES_2));
  }

  @TestWithRole(roles = { APPROVER, MODEL_TUNER, AUDITOR })
  void its200_whenListingFeatureVectors() {
    given(findVectorsByPolicyUseCase.activate(new Paging(PAGE_INDEX, PAGE_SIZE)))
        .willReturn(FEATURE_VECTORS_DTO);
    given(listVectorsQuery.count()).willReturn(2);

    get(LIST_VECTORS_PAGING_URL)
        .statusCode(OK.value())
        .header(HEADER_TOTAL_COUNT, "2")
        .body("columns", is(COLUMNS))
        .body("featureVectors[0].signature", is(SIGNATURE_1))
        .body("featureVectors[0].values", is(FEATURE_VALUES_1))
        .body("featureVectors[1].signature", is(SIGNATURE_2))
        .body("featureVectors[1].values", is(FEATURE_VALUES_2));
  }
}
