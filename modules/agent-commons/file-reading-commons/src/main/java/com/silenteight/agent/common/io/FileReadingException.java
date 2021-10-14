package com.silenteight.agent.common.io;

import static java.lang.String.format;

public class FileReadingException extends RuntimeException {

  private static final long serialVersionUID = -1638977899828865278L;

  public FileReadingException(String path, Exception e) {
    super(format("Can't load file %s", path), e);
  }
}
