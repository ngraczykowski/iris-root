package com.silenteight.warehouse.statistics.controller;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.statistics.controller.StatisticsDataController;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Import({ StatisticsDataController.class })
class StatisticsDataControllerTest extends BaseRestControllerTest {

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  public void getPolicyStatistic_wrongLocalDateFormat_badRequest() throws Exception {
    get("/statistics/123/policy?from=2021-12-123&to=2022-02-12")
        .statusCode(BAD_REQUEST.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  public void getDailyPolicyStatistic_wrongLocalDateFormat_badRequest() throws Exception {
    get("/statistics/123/policyDaily?from=2021-12-123&to=2022-02-12")
        .statusCode(BAD_REQUEST.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  public void getPolicyStatistic_noToParam_badRequest() throws Exception {
    get("/statistics/123/policy?from=2021-12-123")
        .statusCode(BAD_REQUEST.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  public void getDailyPolicyStatistic_noToParam_badRequest() throws Exception {
    get("/statistics/123/policyDaily?from=2021-12-123")
        .statusCode(BAD_REQUEST.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  public void getPolicyStatistic_success() throws Exception {
    get("/statistics/123/policy?from=2021-12-12&to=2022-02-12")
        .statusCode(OK.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  public void getDailyPolicyStatistic_success() throws Exception {
    get("/statistics/123/policyDaily?from=2021-12-12&to=2022-02-12")
        .statusCode(OK.value());
  }
}