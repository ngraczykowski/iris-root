package com.silenteight.warehouse.statistics.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;

@Slf4j
@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
public class StatisticsDataController {

  private static final String POLICY_STATISTICS_URL = "/statistics/{policyId}/policy";
  private static final String POLICY_DAILY_STATISTICS_URL =
      "/statistics/{policyId}/policyDaily";


  @GetMapping(POLICY_STATISTICS_URL)
  @PreAuthorize("isAuthorized('POLICY_STATISTICS')")
  @ResponseBody
  public PolicyStatisticsResponse getPolicyStatistics(
      @PathVariable("policyId") String policyId, @Valid StatisticsRequest params) {
    log.debug("PolicyId: {}, with body: {}", policyId, params);
    return PolicyStatisticsResponse.builder().build();
  }

  @GetMapping(POLICY_DAILY_STATISTICS_URL)
  @PreAuthorize("isAuthorized('POLICY_STATISTICS')")
  @ResponseBody
  public List<DailyPolicyStatisticsResponse> getDailyPolicyStatistics(
      @PathVariable("policyId") String policyId, @Valid StatisticsRequest params) {
    log.debug("PolicyId: {}, with body: {}", policyId, params);
    return List.of(DailyPolicyStatisticsResponse.builder().build());
  }
}
