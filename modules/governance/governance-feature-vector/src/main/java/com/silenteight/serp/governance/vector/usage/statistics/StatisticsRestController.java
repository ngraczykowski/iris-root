package com.silenteight.serp.governance.vector.usage.statistics;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.vector.usage.statistics.dto.StatisticsDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.vector.domain.DomainConstants.VECTOR_ENDPOINT_TAG;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = VECTOR_ENDPOINT_TAG)
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
