package com.silenteight.adjudication.engine.analysis.categoryrequest.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesRequest;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

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

  public int getMatchCount() {
    return missingMatchCategories.stream().mapToInt(MissingMatchCategory::getMatchCount).sum();
  }

  public BatchGetMatchesCategoryValuesRequest toBatchGetMatchCategoryValuesRequestV2() {
    return BatchGetMatchesCategoryValuesRequest.newBuilder()
        .addAllCategoryMatches(
            missingMatchCategories.stream().map(MissingMatchCategory::toCategoryMatches).collect(
                toList()))
        .build();
  }

  public BatchGetMatchCategoryValuesRequest toBatchGetMatchCategoryValuesRequestV1() {
    return BatchGetMatchCategoryValuesRequest
        .newBuilder()
        .addAllMatchValues(missingMatchCategories
            .stream()
            .map(MissingMatchCategory::getMatchCategoryName)
            .collect(toList())
            .stream()
            .flatMap(List::stream)
            .collect(toList()))
        .build();
  }

  public Collection<String> getCategories() {
    return categoryMap.getCategoryNames();
  }

  public void forEachMatch(Consumer<String> matchNameConsumer) {
    missingMatchCategories
        .forEach(missingMatchCategory -> {
          missingMatchCategory
              .getMatches()
              .forEach(match -> matchNameConsumer.accept(match.getName()));
        });
  }
}
