package com.silenteight.warehouse.alert.rest.service;

import static java.lang.String.format;

public class AlertNotFoundException extends Exception {

  private static final long serialVersionUID = -4207507520395834860L;

  AlertNotFoundException(String alertName) {
    super(format("Alert with %s id not found.", alertName));
  }
}
