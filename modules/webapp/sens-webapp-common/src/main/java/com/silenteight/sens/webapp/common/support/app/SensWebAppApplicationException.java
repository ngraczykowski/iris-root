package com.silenteight.sens.webapp.common.support.app;

import org.springframework.boot.ExitCodeGenerator;

import static java.lang.String.format;

public class SensWebAppApplicationException extends RuntimeException implements ExitCodeGenerator {

  private static final long serialVersionUID = 1L;

  private final int exitCode;

  public SensWebAppApplicationException(int exitCode) {
    super(format("SENS application failed (exit code: %d)", exitCode));
    this.exitCode = exitCode;
  }

  @Override
  public int getExitCode() {
    return exitCode;
  }
}
