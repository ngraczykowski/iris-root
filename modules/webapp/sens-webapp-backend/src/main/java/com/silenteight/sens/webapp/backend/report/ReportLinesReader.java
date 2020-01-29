package com.silenteight.sens.webapp.backend.report;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.stream.Stream;

import static io.vavr.control.Try.withResources;

@RequiredArgsConstructor
public class ReportLinesReader {

  private static final String BASE_RESOURCES_PATH = "/reports/";

  @NonNull
  private final String fileName;
  @NonNull
  private final Charset encoding;

  public Stream<String> readLines() {
    return withResources(this::getInputStream)
        .of(is -> IOUtils.toString(is, encoding))
        .map(String::lines)
        .getOrElseThrow(ReportLinesReadException::new);
  }

  private InputStream getInputStream() {
    return getClass().getResourceAsStream(BASE_RESOURCES_PATH + fileName);
  }

  private static class ReportLinesReadException extends RuntimeException {

    private static final long serialVersionUID = 7118483158632167130L;

    public ReportLinesReadException(Throwable cause) {
      super("Could not read lines from Report", cause);
    }
  }
}
