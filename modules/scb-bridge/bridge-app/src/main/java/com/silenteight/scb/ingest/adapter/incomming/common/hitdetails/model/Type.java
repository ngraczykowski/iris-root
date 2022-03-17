package com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@AllArgsConstructor
public enum Type {

  INDIVIDUAL("I"),
  ORGANIZATION("C"),
  NATION("N");

  @NonNull
  @Getter
  private final String abbreviation;

  public static Type parse(@NonNull String abbreviation) {
    return Arrays
        .stream(Type.values())
        .filter(t -> StringUtils.equalsIgnoreCase(abbreviation, t.abbreviation))
        .findFirst()
        .orElseThrow(() -> new TypeNotFoundException(abbreviation));
  }

  public static class TypeNotFoundException extends IllegalArgumentException {

    private static final long serialVersionUID = -3512456700003846367L;

    TypeNotFoundException(@NonNull String abbreviation) {
      super(String.format("Tag not found: %s", abbreviation));
    }
  }
}
