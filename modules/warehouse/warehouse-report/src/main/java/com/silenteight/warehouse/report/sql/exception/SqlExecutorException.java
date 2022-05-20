package com.silenteight.warehouse.report.sql.exception;

public class SqlExecutorException extends RuntimeException {

  private static final long serialVersionUID = 1397834735622066490L;

  public SqlExecutorException(Throwable e) {
    super("Error occurred during processing sql", e);
  }
}
