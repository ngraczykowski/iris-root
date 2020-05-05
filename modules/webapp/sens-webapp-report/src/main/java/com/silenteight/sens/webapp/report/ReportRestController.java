package com.silenteight.sens.webapp.report;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.common.rest.Authority;
import com.silenteight.sens.webapp.common.rest.RestConstants;
import com.silenteight.sens.webapp.common.support.csv.CsvResponseWriter;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(RestConstants.ROOT)
class ReportRestController {

  @NonNull
  private final CsvResponseWriter csvResponseWriter = new CsvResponseWriter();

  @NonNull
  private final ReportProvider reportProvider;

  @GetMapping("/report/{reportName}")
  @PreAuthorize(Authority.AUDITOR)
  public void getReport(
      HttpServletResponse response,
      @PathVariable String reportName,
      @RequestParam Map<String, String> parameters) throws IOException {

    ReportGenerator reportGenerator = reportProvider.getReportGenerator(reportName);
    Report report = reportGenerator.generateReport(parameters);
    csvResponseWriter.write(response, report.getReportFileName(), report.getReportContent());
  }
}
