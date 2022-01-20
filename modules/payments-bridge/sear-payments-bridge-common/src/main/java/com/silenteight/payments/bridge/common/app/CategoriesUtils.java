package com.silenteight.payments.bridge.common.app;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoriesUtils {

  private static final String CATEGORY_PREFIX = "categories/";

  //TODO(jgajewski): remove CATEGORY_MESSAGE_STRUCTURE
  // and replace by CATEGORY_NAME_MESSAGE_STRUCTURE
  public static final String CATEGORY_MESSAGE_STRUCTURE = "messageStructure";
  public static final String CATEGORY_NAME_MESSAGE_STRUCTURE = CATEGORY_PREFIX + "messageStructure";
  public static final String CATEGORY_MESSAGE_STRUCTURE_DISPLAY_NAME = "Message Structure";
}
