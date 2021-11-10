package com.silenteight.warehouse.indexer.query.streaming.exception;

import java.io.IOException;

public class FetchAllDataException extends RuntimeException {

  private static final long serialVersionUID = 1965142664801672664L;

  public FetchAllDataException(IOException e) {
    super(e);
  }
}
