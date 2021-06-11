package com.silenteight.warehouse.indexer.alert;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.indexer.alert.AlertControllerConstants.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    AlertController.class,
    GenericExceptionControllerAdvice.class
})
class AlertControllerTest extends BaseRestControllerTest {

  @MockBean
  AlertService alertService;

  @Test
  @WithMockUser(username = USERNAME, authorities = { QA })
  void its200_whenInvokedGetQaAlertsList() {
    when(alertService.getMultipleAlertsAttributes(any(), any()))
        .thenReturn(ALERT_ATTRIBUTES_LIST_DTO);

    get(QA_ALERT_LIST_URL)
        .statusCode(OK.value())
        .body("alerts[0].attributes.alertId", is(ALERT_ID));
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRoleForGetQaAlertsList() {
    get(QA_ALERT_LIST_URL).statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { QA })
  void its200_whenInvokedGetQaAlert() {
    when(alertService.getSingleAlertAttributes(any(), any()))
        .thenReturn(ALERT_ATTRIBUTES);

    get(QA_ALERT_URL)
        .statusCode(OK.value())
        .body("attributes.alertId", is(ALERT_ID));
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRoleForGetQaAlert() {
    get(QA_ALERT_LIST_URL).statusCode(FORBIDDEN.value());
  }
}
