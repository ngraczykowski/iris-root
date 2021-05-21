package com.silenteight.adjudication.engine.analysis.categoryrequest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class MissingCategoryResult {

  private final List<String> missingMatchCategories;
  private final Map<String, Long> categories;

  public void addMissingMatchCategory(String missingMatchCategory) {
    this.missingMatchCategories.add(missingMatchCategory);
  }

  public void addCategoryMapping(String categoryKey, Long categoryId) {
    this.categories.putIfAbsent(categoryKey, categoryId);
  }

}
