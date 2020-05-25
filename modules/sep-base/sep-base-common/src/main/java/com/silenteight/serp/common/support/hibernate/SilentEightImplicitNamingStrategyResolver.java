package com.silenteight.serp.common.support.hibernate;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
class SilentEightImplicitNamingStrategyResolver implements Serializable {

  private static final long serialVersionUID = 701394058931930236L;

  private final String tablePrefix;

  String resolvePrimaryTableName(String original) {
    return tablePrefix + removeEntitySuffix(original);
  }

  String resolveCollectionTableName(String original) {
    return tablePrefix + removeEntitySuffixFromOwningTableName(original);
  }

  private static String removeEntitySuffixFromOwningTableName(String original) {
    int index = original.lastIndexOf('_');
    if (index >= 0)
      return removeEntitySuffix(original.substring(0, index)) + original.substring(index);
    return original;
  }

  private static String removeEntitySuffix(String original) {
    int indexOfEntity = original.toLowerCase().lastIndexOf("entity");
    return indexOfEntity >= 0 ? original.substring(0, indexOfEntity) : original;
  }
}
