package com.silenteight.warehouse.report.create;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.common.domain.ReportConstants.IS_PRODUCTION;
import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static java.lang.String.format;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;
import static org.springframework.http.HttpStatus.SEE_OTHER;

@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
@Slf4j
public class CreateReportRestController {

  private static final String COMMON_URL = "/v3/analysis/{type}/reports/{name}";

  @NonNull
  private final ReportRequestService reportRequestService;

  @PostMapping(COMMON_URL)
  public ResponseEntity<Void> createReport(
      @DateTimeFormat(iso = DATE_TIME)
      @RequestParam(required = false) OffsetDateTime from,
      @DateTimeFormat(iso = DATE_TIME)
      @RequestParam(required = false) OffsetDateTime to,
      @PathVariable String type,
      @PathVariable String name) {

    if (IS_PRODUCTION.test(type))
      validate(from, to);

    log.info("Create report request received, {} - from={} - to={}", name, from, to);

    ReportInstanceReferenceDto reportInstanceReference
        = reportRequestService.request(from, to, type, name);

    log.debug(
        "Create report request processed, from={} - to={}, reportId={}",
        from, to, reportInstanceReference.getReferenceId());

    return ResponseEntity.status(SEE_OTHER)
        .header(
            "Location",
            getReportLocation(type, reportInstanceReference.getReferenceId()))
        .build();
  }

  private static void validate(OffsetDateTime from, OffsetDateTime to) {
    if (from == null || to == null) {
      throw new IllegalStateException("Date parameters are mandatory for production report");
    }
  }

  private static String getReportLocation(String type, long id) {
    return format("%s/%d/status", type, id);
  }

}
