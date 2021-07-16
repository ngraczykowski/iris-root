package com.silenteight.serp.governance.qa.manage.domain.exception;

public class WrongDiscriminatorException extends RuntimeException {

  private static final long serialVersionUID = -4053597311211083842L;

  public WrongDiscriminatorException(String discriminator) {
    super(String.format("Could not find alert with discriminator=%s", discriminator));
  }
}
