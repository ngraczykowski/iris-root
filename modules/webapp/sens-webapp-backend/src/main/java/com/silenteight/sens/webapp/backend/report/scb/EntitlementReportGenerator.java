package com.silenteight.sens.webapp.backend.report.scb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.report.Report;
import com.silenteight.sens.webapp.backend.report.ReportGenerator;
import com.silenteight.sens.webapp.common.support.csv.LinesSupplier;
import com.silenteight.sens.webapp.common.support.csv.SimpleLinesSupplier;
import com.silenteight.sens.webapp.common.time.DateFormatter;
import com.silenteight.sens.webapp.common.time.TimeSource;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class EntitlementReportGenerator implements ReportGenerator {

  private static final String REPORT_NAME = "entitlement-report";

  private static final String FILENAME = REPORT_NAME + ".csv";
  private static final Charset ENCODING = StandardCharsets.UTF_8;

  private final TimeSource timeProvider;
  private final DateFormatter timeFormatter;

  @Override
  public String getName() {
    return REPORT_NAME;
  }

  @Override
  public Report generateReport() {
    return new EntitlementReport(getReportData());
  }

  private List<String> getReportData() {
    //TODO(kdzieciol): Update this report after PO's decision, which roles are valid
    try (InputStream inputStream = getInputStream()) {
      String report = IOUtils.toString(inputStream, ENCODING);
      String date = timeFormatter.format(timeProvider.now());
      return String.format(report, date).lines().collect(toList());
    } catch (Exception e) {
      throw new EntitlementReportException(e);
    }
  }

  private InputStream getInputStream() {
    return getClass().getResourceAsStream("/" + FILENAME);
  }

  @RequiredArgsConstructor
  private static class EntitlementReport implements Report {

    private final List<String> data;

    @Override
    public String getReportFileName() {
      return FILENAME;
    }

    @Override
    public LinesSupplier getReportContent() {
      return new SimpleLinesSupplier(data);
    }
  }

  private static class EntitlementReportException extends RuntimeException {

    private static final long serialVersionUID = 7118483158632167130L;

    public EntitlementReportException(Exception e) {
      super("Could not generate entitlement Report", e);
    }
  }
}
