package com.silenteight.warehouse.indexer.alert;

class ZeroMatchesException extends RuntimeException {

  private static final long serialVersionUID = 5629896225365335821L;

  ZeroMatchesException(String message) {
    super(message);
  }
}
