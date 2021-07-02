package com.silenteight.warehouse.common.opendistro.utils;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class OpendistroUtils {

  private static final String KEYWORD_SUFFIX_SEARCH = ".keyword";

  public static String getRawField(String fieldname) {
    return fieldname + KEYWORD_SUFFIX_SEARCH;
  }
}
