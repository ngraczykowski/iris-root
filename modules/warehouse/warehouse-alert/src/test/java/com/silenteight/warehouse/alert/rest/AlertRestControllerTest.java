package com.silenteight.warehouse.alert.rest;

import com.silenteight.warehouse.alert.rest.service.AlertProvider;
import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.alert.rest.AlertControllerConstants.ALERT_ATTRIBUTES_LIST;
import static com.silenteight.warehouse.alert.rest.AlertControllerConstants.DISCRIMINATOR_ID;
import static com.silenteight.warehouse.alert.rest.AlertControllerConstants.QA_ALERT_LIST_URL;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.QA;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.USER_ADMINISTRATOR;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    AlertRestController.class,
    GenericExceptionControllerAdvice.class,
    AlertRestControllerAdvice.class
})
class AlertRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private AlertProvider alertProvider;

  @Test
  @WithMockUser(username = USERNAME, authorities = { QA })
  void its200_whenInvokedGetQaAlertsList() {
    when(alertProvider.getMultipleAlertsAttributes(any(), any()))
        .thenReturn(ALERT_ATTRIBUTES_LIST);

    get(QA_ALERT_LIST_URL)
        .statusCode(OK.value())
        .body("[0].s8_discriminator", containsString(DISCRIMINATOR_ID));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its403_whenNotPermittedRoleForGetQaAlertsList() {
    get(QA_ALERT_LIST_URL).statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its403_whenNotPermittedRoleForGetQaAlert() {
    get(QA_ALERT_LIST_URL).statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its403_whenNotPermittedRoleForPostQaAlert() {
    get(QA_ALERT_LIST_URL).statusCode(FORBIDDEN.value());
  }

}
