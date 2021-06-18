package com.silenteight.warehouse.management.group.domain.exception;

import java.util.UUID;

import static java.lang.String.format;

public class CountryGroupDoesNotExistException extends RuntimeException {

  private static final long serialVersionUID = 6890131510423105444L;

  public CountryGroupDoesNotExistException(UUID id) {
    super(format("Country Group with UUID %s does not exist.", id));
  }
}
