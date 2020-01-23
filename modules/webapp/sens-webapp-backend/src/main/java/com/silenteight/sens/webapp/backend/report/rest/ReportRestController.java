package com.silenteight.sens.webapp.backend.report.rest;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.report.Report;
import com.silenteight.sens.webapp.backend.report.ReportGenerator;
import com.silenteight.sens.webapp.backend.report.ReportProvider;
import com.silenteight.sens.webapp.backend.support.CsvResponseWriter;
import com.silenteight.sens.webapp.common.rest.RestConstants;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
  public void getReport(
      HttpServletResponse response, @PathVariable String reportName) throws IOException {

    ReportGenerator reportGenerator = reportProvider.getReportGenerator(reportName);
    Report report = reportGenerator.generateReport();
    csvResponseWriter.write(response, report.getReportFileName(), report.getReportContent());
  }
}
