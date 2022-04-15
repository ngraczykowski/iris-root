package com.silenteight.sens.webapp.common.support.csv;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

public class CsvResponseWriter {

  private static final String MEDIA_TYPE_TEXT_CSV = "text/csv";

  public void write(
      HttpServletResponse response,
      String filename,
      LinesSupplier linesSupplier) throws IOException {

    response.addHeader("Content-Type", MEDIA_TYPE_TEXT_CSV);
    response.addHeader("Content-Disposition", "attachment; filename=" + filename);
    response.setCharacterEncoding("UTF-8");

    writeToOutput(response.getWriter(), linesSupplier);
  }

  private static void writeToOutput(PrintWriter writer, LinesSupplier linesSupplier) {
    linesSupplier.lines().forEach(line -> appendLine(writer, line));
  }

  @SuppressWarnings("findsecbugs:XSS_SERVLET")
  private static void appendLine(PrintWriter writer, String line) {
    writer.append(line);
    writer.println();
  }
}
