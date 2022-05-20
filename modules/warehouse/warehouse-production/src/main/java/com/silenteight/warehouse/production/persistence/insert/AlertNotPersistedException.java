package com.silenteight.warehouse.production.persistence.insert;

import static java.lang.String.format;

class AlertNotPersistedException extends RuntimeException {

  private static final long serialVersionUID = 4719757856497784133L;

  public AlertNotPersistedException(String discriminator) {
    super(format("Alert with discriminator=%s not persisted", discriminator));
  }
}
