package com.silenteight.universaldatasource.api.library.category.v2;

import com.silenteight.datasource.categories.api.v2.CategoryType;

public enum CategoryTypeShared {
  ENUMERATED,
  ANY_STRING,
  CATEGORY_TYPE_UNSPECIFIED;

  static CategoryType toCategoryTypeProto(CategoryTypeShared categoryType) {
    switch (categoryType) {
      case ENUMERATED:
        return CategoryType.ENUMERATED;
      case ANY_STRING:
        return CategoryType.ANY_STRING;
      default:
        return CategoryType.CATEGORY_TYPE_UNSPECIFIED;
    }
  }

  static CategoryTypeShared toCategoryTypeShared(CategoryType categoryType) {
    switch (categoryType) {
      case ENUMERATED:
        return CategoryTypeShared.ENUMERATED;
      case ANY_STRING:
        return CategoryTypeShared.ANY_STRING;
      default:
        return CategoryTypeShared.CATEGORY_TYPE_UNSPECIFIED;
    }
  }
}
