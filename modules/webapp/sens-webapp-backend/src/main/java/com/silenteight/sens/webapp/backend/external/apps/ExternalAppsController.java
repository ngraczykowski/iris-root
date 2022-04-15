package com.silenteight.sens.webapp.backend.external.apps;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import static com.silenteight.sens.webapp.backend.external.apps.ExternalAppsController.APP_ENDPOINT_TAG;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sep.base.common.logging.LogMarkers.INTERNAL;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = APP_ENDPOINT_TAG)
class ExternalAppsController {

  private static final String REPORTING_APP_NAME = "reporting";
  protected static final String APP_ENDPOINT_TAG = "App";

  @NonNull
  private final String reportingUrl;

  ExternalAppsController(@NonNull ReportingUrlProvider reportingUrlProvider) {
    this.reportingUrl = reportingUrlProvider.getReportingUrl();
  }

  @GetMapping(value = "/apps/reporting")
  @PreAuthorize("isAuthorized('REDIRECT_TO_REPORTING')")
  public void redirect(HttpServletResponse response) {
    log.info(INTERNAL, "Redirect to reportingURL");
    response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
    response.setHeader("Location", response.encodeRedirectURL(reportingUrl));
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
