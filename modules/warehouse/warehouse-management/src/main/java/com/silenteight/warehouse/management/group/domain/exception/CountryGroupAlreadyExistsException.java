package com.silenteight.warehouse.management.group.domain.exception;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static java.lang.String.format;

public class CountryGroupAlreadyExistsException extends RuntimeException {

  private static final long serialVersionUID = 554906536632725129L;

  public CountryGroupAlreadyExistsException(UUID id, DataIntegrityViolationException e) {
    super(format("Country Group with UUID %s already exists.", id), e);
  }
}
