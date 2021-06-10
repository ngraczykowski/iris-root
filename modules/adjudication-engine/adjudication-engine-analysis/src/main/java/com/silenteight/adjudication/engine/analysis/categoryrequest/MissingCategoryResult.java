package com.silenteight.adjudication.engine.analysis.categoryrequest;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesRequest;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class MissingCategoryResult {

  @NonNull
  private final List<String> missingMatchCategories;
  @Getter
  @NonNull
  private final CategoryMap categoryMap;

  boolean isEmpty() {
    return missingMatchCategories.isEmpty();
  }

  int getCount() {
    return missingMatchCategories.size();
  }

  BatchGetMatchCategoryValuesRequest toBatchGetMatchCategoryValuesRequest() {
    return BatchGetMatchCategoryValuesRequest.newBuilder()
        .addAllMatchValues(missingMatchCategories)
        .build();
  }

  Collection<String> getCategories() {
    return categoryMap.getCategoryNames();
  }

  void forEachMatch(Consumer<String> matchNameConsumer) {
    missingMatchCategories
        .stream()
        .map(categoryValueName -> {
          var resourceName = ResourceName.create(categoryValueName);
          resourceName.remove("categories");
          return resourceName.getPath();
        })
        .forEach(matchNameConsumer);
  }
}
