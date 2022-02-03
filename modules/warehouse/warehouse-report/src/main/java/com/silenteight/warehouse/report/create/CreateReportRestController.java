package com.silenteight.warehouse.report.create;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Set;

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
  private static final String GROUP_NAME = "kibana-sso";

  @NonNull
  private final ReportRequestService reportRequestService;

  @NonNull
  private final UserAwareTokenProvider userAwareTokenProvider;

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

    Set<String> dataAccessRoles = userAwareTokenProvider.getRolesForGroup(GROUP_NAME);

    log.info("Create report request received, "
        + "reportName={}, from={}, to={}, dataAccessRoles={}", name, from, to, dataAccessRoles);

    ReportInstanceReferenceDto reportInstanceReference
        = reportRequestService.request(from, to, type, name, dataAccessRoles);

    log.debug("Create report request processed, "
            + "reportName={}, from={}, to={}, dataAccessRoles={}, reportId={}",
        name, from, to, dataAccessRoles, reportInstanceReference.getReferenceId());

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
