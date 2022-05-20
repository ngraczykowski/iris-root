package com.silenteight.adjudication.engine.analysis.categoryrequest.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesRequest;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class MissingCategoryResult {

  @NonNull
  private final List<MissingMatchCategory> missingMatchCategories;
  @Getter
  @NonNull
  private final CategoryMap categoryMap;

  public boolean isEmpty() {
    return missingMatchCategories.isEmpty();
  }

  public int getCount() {
    return missingMatchCategories.size();
  }

  public int getMatchValueCount() {
    return missingMatchCategories.stream().mapToInt(MissingMatchCategory::getMatchCount).sum();
  }

  public BatchGetMatchesCategoryValuesRequest toBatchGetMatchCategoryValuesRequest() {
    return BatchGetMatchesCategoryValuesRequest.newBuilder()
        .addAllCategoryMatches(
            missingMatchCategories
                .stream()
                .map(MissingMatchCategory::toCategoryMatches)
                .collect(toList()))
        .build();
  }

  public Collection<String> getCategories() {
    return categoryMap.getCategoryNames();
  }
}
