package com.silenteight.sens.webapp.report;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.common.support.csv.CsvResponseWriter;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.silenteight.sens.webapp.common.rest.RestConstants.*;
import static com.silenteight.sens.webapp.common.support.request.IpAddressExtractor.from;
import static com.silenteight.sens.webapp.report.ReportRestController.REPORT_ENDPOINT_TAG;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(ROOT)
@Tag(name = REPORT_ENDPOINT_TAG)
class ReportRestController {

  protected static final String REPORT_ENDPOINT_TAG = "Report";

  @NonNull
  private final CsvResponseWriter csvResponseWriter = new CsvResponseWriter();

  @NonNull
  private final ReportGeneratorFacade reportGeneratorFacade;

  @GetMapping("/reports/{reportName}")
  @PreAuthorize("isAuthorized('GENERATE_REPORT')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = OK_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = BAD_REQUEST_STATUS, description = BAD_REQUEST_DESCRIPTION),
      @ApiResponse(responseCode = NOT_FOUND_STATUS, description = NOT_FOUND_DESCRIPTION)
  })
  public void getReport(
      HttpServletRequest request,
      HttpServletResponse response,
      @PathVariable String reportName,
      @RequestParam Map<String, String> parameters) throws IOException {

    log.debug("Generate report: reportName={}, parameters={}", reportName, parameters);
    Report report = reportGeneratorFacade.generate(reportName, from(request), parameters);
    csvResponseWriter.write(response, report.getReportFileName(), report.getReportContent());
  }
}
