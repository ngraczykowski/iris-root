package com.silenteight.adjudication.engine.analysis.categoryrequest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CategoryMap {

  @NonNull
  private final Map<String, Long> nameToId;

  CategoryMap() {
    nameToId = Map.of();
  }

  Collection<String> getCategoryNames() {
    return nameToId.keySet();
  }

  public long getCategoryId(String categoryName) {
    if (!nameToId.containsKey(categoryName)) {
      throw new UnknownCategoryException(categoryName);
    }

    return nameToId.get(categoryName);
  }
}
