package com.silenteight.serp.governance.migrate;

public class MigrationException extends RuntimeException {

  private static final long serialVersionUID = 3973166058033740695L;

  public MigrationException(Exception e) {
    super("Problem during data migration", e);
  }

  public MigrationException(String msg) {
    super(msg);
  }
}
