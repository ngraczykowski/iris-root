package com.silenteight.warehouse.report.availablereports;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.availablereports.ReportTypeListDto.ReportTypeDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(ROOT)
public class AvailableReportsRestController {

  private static final String COMMON_URL = "/v2/analysis/{type}/reports";

  @NonNull
  private final ReportListService reportListService;

  @GetMapping(COMMON_URL)
  public ResponseEntity<List<ReportTypeDto>> getAvailableReports(@PathVariable String type) {
    return ResponseEntity.ok(reportListService.getReportsList(type));
  }
}
