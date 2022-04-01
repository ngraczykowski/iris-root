package com.silenteight.qco.infrastructure.parser;

import java.io.Serial;

class SolutionConfigurationFileParserException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -7908766630573537582L;

  SolutionConfigurationFileParserException(Throwable cause) {
    super(cause);
  }
}
