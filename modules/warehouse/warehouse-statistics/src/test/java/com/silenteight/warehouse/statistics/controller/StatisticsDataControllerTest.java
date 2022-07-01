package com.silenteight.warehouse.statistics.controller;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.statistics.get.DashboardStatisticsRestController;
import com.silenteight.warehouse.statistics.get.GetDashboardStatisticsUseCase;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Import(DashboardStatisticsRestController.class)
class StatisticsDataControllerTest extends BaseRestControllerTest {

  @MockBean private GetDashboardStatisticsUseCase dashboardStatisticsUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  public void getPolicyStatistic_wrongLocalDateFormat_badRequest() {
    get("/statistics?from=2021-12-123&to=2022-02-12").statusCode(BAD_REQUEST.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  public void getDailyPolicyStatistic_wrongLocalDateFormat_badRequest() {
    get("/statistics/daily?from=2021-12-123&to=2022-02-12").statusCode(BAD_REQUEST.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  public void getPolicyStatistic_noToParam_badRequest() {
    get("/statistics/daily?from=2021-12-123").statusCode(BAD_REQUEST.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  public void getDailyPolicyStatistic_noToParam_badRequest() {
    get("/statistics?from=2021-12-123").statusCode(BAD_REQUEST.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  public void getPolicyStatistic_success() {
    get("/statistics?from=2021-12-12&to=2022-02-12").statusCode(OK.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  public void getDailyPolicyStatistic_success() {
    get("/statistics/daily?from=2021-12-12&to=2022-02-12").statusCode(OK.value());
  }
}
