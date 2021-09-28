package com.silenteight.serp.governance.file.common.exception;

import java.util.List;

import static java.lang.String.format;

public class WrongFilesResourceFormatException extends RuntimeException {

  private static final long serialVersionUID = 2561376825237814931L;

  public WrongFilesResourceFormatException(List<String> filesResources) {
    super(format("Wrong files resource format, %s ", filesResources.toArray()));
  }

  public WrongFilesResourceFormatException(String fileResourceName) {
    super(String.format("Wrong file resource format, %s", fileResourceName));
  }
}
