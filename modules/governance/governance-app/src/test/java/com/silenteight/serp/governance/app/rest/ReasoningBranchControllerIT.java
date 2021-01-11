package com.silenteight.serp.governance.app.rest;

import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchSummary;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.serp.governance.branchquery.ReasoningBranchFinder;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import javax.persistence.EntityNotFoundException;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

@Import({ ReasoningBranchController.class, GenericExceptionControllerAdvice.class })
class ReasoningBranchControllerIT extends BaseRestControllerTest {

  private static final long DECISION_TREE_ID = 1L;

  @MockBean
  private ReasoningBranchFinder finder;

  @Test
  void shouldProduceSuccessOutput_whenFinderReturnsEmptyBranches() {
    when(finder.findAllByDecisionTreeId(1)).thenReturn(emptyList());

    get("/v1/decision-trees/1/reasoning-branches")
        .statusCode(HttpStatus.OK.value())
        .body("size()", is(0));
  }

  @Test
  void shouldMapOutputJsonCorrectly() {
    when(finder.findAllByDecisionTreeId(DECISION_TREE_ID)).thenReturn(asList(
        createSummary(1L),
        createSummary(2L)));

    get("/v1/decision-trees/1/reasoning-branches")
        .statusCode(HttpStatus.OK.value())
        .body("size()", is(2))
        .body("[0].decisionTreeId", equalTo(1))
        .body("[0].featureVectorId", equalTo(1))
        .body("[0].featureValues", hasItems("feature1", "feature2"))
        .body("[0].solution", equalTo("BRANCH_FALSE_POSITIVE"))
        .body("[0].enabled", equalTo(true))
        .body("[1].decisionTreeId", equalTo(1))
        .body("[1].featureVectorId", equalTo(2))
        .body("[1].featureValues", hasItems("feature1", "feature2"))
        .body("[1].solution", equalTo("BRANCH_FALSE_POSITIVE"))
        .body("[1].enabled", equalTo(true));
  }

  @Test
  void shouldReturnSingleBranchMappedCorrectly() {
    when(finder.getByDecisionTreeIdAndFeatureVectorId(DECISION_TREE_ID, 2))
        .thenReturn(createSummary(2L));

    get("/v1/decision-trees/1/reasoning-branches/2")
        .statusCode(HttpStatus.OK.value())
        .body("decisionTreeId", equalTo(1))
        .body("featureVectorId", equalTo(2))
        .body("featureValues", hasItems("feature1", "feature2"))
        .body("solution", equalTo("BRANCH_FALSE_POSITIVE"))
        .body("enabled", equalTo(true));
  }

  // TODO(iwnek) Temporary disabled as error mappings not working (See RestControllerTestBase)
  @Disabled
  @Test
  void shouldReturnNotFound_whenFinderThrowsEntityNotFoundException() {
    when(finder.getByDecisionTreeIdAndFeatureVectorId(DECISION_TREE_ID, 2))
        .thenThrow(EntityNotFoundException.class);

    get("/v1/decision-trees/1/reasoning-branches/2")
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

  // TODO(iwnek) Temporary disabled as error mappings not working (See RestControllerTestBase)
  @Disabled
  @Test
  void shouldReturnInternalServerError_whenFinderThrowsUnexpectedException() {
    when(finder.getByDecisionTreeIdAndFeatureVectorId(DECISION_TREE_ID, 2))
        .thenThrow(RuntimeException.class);

    get("/v1/decision-trees/1/reasoning-branches/2")
        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  private static ReasoningBranchSummary createSummary(
      long featureVectorId) {
    return ReasoningBranchSummary.newBuilder()
        .setEnabled(true)
        .setReasoningBranchId(ReasoningBranchId.newBuilder()
            .setDecisionTreeId(DECISION_TREE_ID)
            .setFeatureVectorId(featureVectorId)
            .build())
        .setSolution(BranchSolution.BRANCH_FALSE_POSITIVE)
        .addAllFeatureValue(asList("feature1", "feature2"))
        .build();
  }
}
