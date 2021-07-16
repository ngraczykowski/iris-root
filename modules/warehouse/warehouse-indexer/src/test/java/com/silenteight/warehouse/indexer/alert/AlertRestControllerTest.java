package com.silenteight.warehouse.indexer.alert;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.QA;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.USER_ADMINISTRATOR;
import static com.silenteight.warehouse.indexer.alert.AlertControllerConstants.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Import({
    AlertRestController.class,
    GenericExceptionControllerAdvice.class,
    AlertRestControllerAdvice.class
})
class AlertRestControllerTest extends BaseRestControllerTest {

  @MockBean
  AlertQueryService alertQueryService;

  @Test
  @WithMockUser(username = USERNAME, authorities = { QA })
  void its200_whenInvokedGetQaAlertsList() {
    when(alertQueryService.getMultipleAlertsAttributes(any(), any()))
        .thenReturn(ALERT_ATTRIBUTES_LIST);

    get(QA_ALERT_LIST_URL)
        .statusCode(OK.value())
        .body("[0].s8_discriminator", containsString(DISCRIMINATOR_ID));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { QA })
  void its404_whenAtLeastOneDiscriminatorNotExists() {
    when(alertQueryService.getMultipleAlertsAttributes(any(), any()))
        .thenThrow(AlertNotFoundException.class);

    get(QA_ALERT_LIST_URL).statusCode(NOT_FOUND.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its403_whenNotPermittedRoleForGetQaAlertsList() {
    get(QA_ALERT_LIST_URL).statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { QA })
  void its200_whenInvokedGetQaAlert() {
    when(alertQueryService.getSingleAlertAttributes(any(), any()))
        .thenReturn(ALERT_ATTRIBUTES);

    get(QA_ALERT_URL)
        .statusCode(OK.value())
        .body("s8_discriminator", containsString(DISCRIMINATOR_ID));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { QA })
  void its404_whenAlertNotExists() {
    when(alertQueryService.getSingleAlertAttributes(any(), any()))
        .thenThrow(AlertNotFoundException.class);

    get(QA_ALERT_URL).statusCode(NOT_FOUND.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its403_whenNotPermittedRoleForGetQaAlert() {
    get(QA_ALERT_LIST_URL).statusCode(FORBIDDEN.value());
  }
}
