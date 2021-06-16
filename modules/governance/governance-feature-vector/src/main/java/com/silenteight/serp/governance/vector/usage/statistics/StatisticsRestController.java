package com.silenteight.serp.governance.vector.usage.statistics;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.vector.usage.statistics.dto.StatisticsDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class StatisticsRestController {

  private static final String STATISTICS_URL = "/v1/vectors/statistics";

  @NonNull
  private final UsageCountQuery usageCountQuery;

  @GetMapping(value = STATISTICS_URL)
  @PreAuthorize("isAuthorized('VIEW_VECTORS_STATISTICS')")
  public ResponseEntity<StatisticsDto> list(@RequestParam String signature) {
    long count = usageCountQuery.getUsageCount(signature);
    return ResponseEntity.ok(new StatisticsDto(count));
  }
}
