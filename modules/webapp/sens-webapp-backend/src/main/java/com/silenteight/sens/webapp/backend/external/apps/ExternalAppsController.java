package com.silenteight.sens.webapp.backend.external.apps;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sep.base.common.logging.LogMarkers.INTERNAL;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@Slf4j
class ExternalAppsController {

  private static final String REPORTING_APP_NAME = "reporting";

  @NonNull
  private final String reportingUrl;

  ExternalAppsController(@NonNull ReportingUrlProvider reportingUrlProvider) {
    this.reportingUrl = reportingUrlProvider.getReportingUrl();
  }

  @GetMapping(value = "/apps/reporting")
  @PreAuthorize("isAuthorized('REDIRECT_TO_REPORTING')")
  public void redirect(HttpServletResponse response) throws IOException {
    log.info(INTERNAL, "Redirect to reportingURL");
    response.sendRedirect(reportingUrl);
    log.info(INTERNAL, "Redirect to = " + reportingUrl);
  }

  @GetMapping(value = "/apps/list")
  public ResponseEntity<AppsListDto> listApps() {
    List<String> result = new ArrayList<>();
    if (StringUtils.isNotEmpty(reportingUrl))
      result.add(REPORTING_APP_NAME);

    return ok(AppsListDto.of(result));
  }
}
