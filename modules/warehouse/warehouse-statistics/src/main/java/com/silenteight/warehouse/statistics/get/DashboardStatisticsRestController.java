package com.silenteight.warehouse.statistics.get;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
@ConditionalOnProperty(value = "warehouse.statistics.daily.recommendation-enabled")
public class DashboardStatisticsRestController {

  private static final String STATISTICS_URL = "/statistics";
  private static final String DAILY_STATISTICS_URL = "/statistics/daily";

  @NonNull
  private final GetDashboardStatisticsUseCase dashboardUseCase;

  @GetMapping(STATISTICS_URL)
  @PreAuthorize("isAuthorized('RECOMMENDATION_STATISTICS')")
  public ResponseEntity<StatisticsResponse> getStatistics(
      @DateTimeFormat(iso = DATE) @RequestParam LocalDate from,
      @DateTimeFormat(iso = DATE) @RequestParam LocalDate to
  ) {
    return ok().body(dashboardUseCase.getTotalCount(StatisticsRequest.of(from, to)));
  }

  @GetMapping(DAILY_STATISTICS_URL)
  @PreAuthorize("isAuthorized('RECOMMENDATION_STATISTICS')")
  public ResponseEntity<List<DailyRecommendationStatisticsResponse>> getDailyStatistics(
      @DateTimeFormat(iso = DATE) @RequestParam LocalDate from,
      @DateTimeFormat(iso = DATE) @RequestParam LocalDate to
  ) {
    return ok().body(dashboardUseCase.getTotalDailyCount(StatisticsRequest.of(from, to)));
  }
}
