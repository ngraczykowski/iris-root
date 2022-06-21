package com.silenteight.sens.webapp.report.list;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.silenteight.sep.auth.authentication.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(ROOT)
@Tag(name = ListReportRestController.REPORT_ENDPOINT_TAG)
public class ListReportRestController {

  protected static final String REPORT_ENDPOINT_TAG = "Report";

  @NonNull
  private final ListReportsQuery listReportsQuery;

  @GetMapping("/reports")
  @PreAuthorize("isAuthorized('LIST_REPORTS')")
  public ResponseEntity<Collection<ReportDto>> listReports() {
    log.debug("List reports");
    return ok(listReportsQuery.listAll());
  }
}
