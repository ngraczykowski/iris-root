package com.silenteight.sens.webapp.backend.support;

import com.silenteight.sens.webapp.common.support.csv.CsvBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletResponse;

public class CsvResponseWriter {

  private static final String MEDIA_TYPE_TEXT_CSV = "text/csv";

  public <T> void write(
      HttpServletResponse response, String filename, CsvBuilder<T> builder) throws IOException {

    response.addHeader("Content-Type", MEDIA_TYPE_TEXT_CSV);
    response.addHeader("Content-Disposition", "attachment; filename=" + filename);
    response.setCharacterEncoding("UTF-8");

    writeToOutput(response.getWriter(), builder);
  }

  @SuppressWarnings("findsecbugs:XSS_SERVLET")
  private static <T> void writeToOutput(PrintWriter writer, CsvBuilder<T> builder) {
    Stream<String> lines = builder.buildLines();
    lines.forEach(writer::append);
  }
}
