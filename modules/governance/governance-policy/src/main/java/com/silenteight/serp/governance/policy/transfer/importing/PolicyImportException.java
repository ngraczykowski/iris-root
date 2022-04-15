package com.silenteight.serp.governance.policy.transfer.importing;

class PolicyImportException extends RuntimeException {

  private static final long serialVersionUID = 8951437342138904064L;

  PolicyImportException(Exception e) {
    super("Problem during Step Policy import", e);
  }

  PolicyImportException(String msg) {
    super(msg);
  }
}
