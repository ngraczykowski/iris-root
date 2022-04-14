package com.silenteight.fab.dataprep.domain.ex;

public class DataPrepException extends RuntimeException {

  private static final long serialVersionUID = -3160244520753000382L;

  public DataPrepException(String msg) {
    super(msg);
  }

  public DataPrepException(Throwable e) {
    super(e);
  }
}
