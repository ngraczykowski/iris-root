package com.silenteight.serp.governance.policy.transform.rbs;

class RbsParserException extends RuntimeException {

  private static final long serialVersionUID = -3121625950227672265L;

  RbsParserException(Exception e) {
    super("Problem during parsing RBS import", e);
  }

  RbsParserException(String msg) {
    super(msg);
  }
}
