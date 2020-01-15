package com.silenteight.sens.webapp.backend.rest;

import lombok.AllArgsConstructor;

import com.silenteight.sens.webapp.backend.presentation.dto.dashboard.DashboardJobDto;
import com.silenteight.sens.webapp.backend.presentation.dto.dashboard.DashboardJobSearchFilterDto;
import com.silenteight.sens.webapp.common.rest.RestConstants;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Collections.emptyList;

@AllArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
public class DashboardRestController {

  @GetMapping(value = "/dashboard/jobs")
  public ResponseEntity<List<String>> getJobNames() {
    return ResponseEntity.ok(emptyList());
  }

  @GetMapping(value = "/dashboard/jobs/{jobName}")
  public ResponseEntity<List<DashboardJobDto>> getJobs(
      @PathVariable String jobName,
      @RequestParam(required = false) boolean details,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int pageSize) {

    DashboardJobSearchFilterDto filter = DashboardJobSearchFilterDto
        .builder()
        .jobName(jobName)
        .details(details)
        .build();

    return ResponseEntity.ok(emptyList());
  }
}
