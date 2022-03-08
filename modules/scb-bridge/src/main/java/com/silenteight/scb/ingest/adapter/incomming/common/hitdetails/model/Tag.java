package com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@AllArgsConstructor
public enum Tag {
  SEARCH_CODE("COD"), NAME("NAM"), PASSPORT("PSP"), NATIONAL_ID("NID");

  @NonNull
  @Getter
  private final String abbreviation;

  public static Tag parse(@NonNull String abbreviation) {
    return Arrays
        .stream(Tag.values())
        .filter(t -> StringUtils.equalsIgnoreCase(abbreviation, t.abbreviation))
        .findFirst()
        .orElseThrow(() -> new TagNotFoundException(abbreviation));
  }

  public static class TagNotFoundException extends IllegalStateException {

    private static final long serialVersionUID = -475905943269554769L;

    TagNotFoundException(@NonNull String abbreviation) {
      super(String.format("Tag not found: %s", abbreviation));
    }
  }
}
