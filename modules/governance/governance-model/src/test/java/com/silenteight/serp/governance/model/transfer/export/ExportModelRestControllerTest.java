package com.silenteight.serp.governance.model.transfer.export;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.model.transfer.export.ExportModelFixtures.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    ExportModelRestController.class,
    GenericExceptionControllerAdvice.class
})
class ExportModelRestControllerTest extends BaseRestControllerTest {

  private static final String EXPORT_MODEL_URL = "/v1/solvingModels/" + MODEL_ID + "/export";

  @MockBean
  private ExportModelUseCase exportModelUseCase;

  @TestWithRole(roles = {})
  @Disabled("No one have permission to 'EXPORT_MODEL'")
  void its200_whenModelExported() {
    given(exportModelUseCase.applyByName(MODEL_ID)).willReturn(transferredModelRoot());

    get(EXPORT_MODEL_URL)
        .statusCode(OK.value())
        .body("checksum", is(CHECKSUM))
        .body("model.metadata.modelId", is(MODEL_ID.toString()))
        .body("model.metadata.approvedBy", is(APPROVED_BY))
        .body("model.policy.metadata.createdBy", is(POLICY_CREATED_BY))
        .body("model.policy.policy.policyId", is(POLICY_ID.toString()))
        .body("model.policy.policy.policyName", is(POLICY_NAME))
        .body("model.policy.policy.description", is(POLICY_DESCRIPTION));
  }

  @TestWithRole(roles = {
      APPROVER, USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER, MODEL_TUNER })
  void its403_whenNotPermittedRole() {
    get(EXPORT_MODEL_URL).statusCode(FORBIDDEN.value());
  }
}
