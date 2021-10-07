package com.silenteight.adjudication.engine.analysis.categoryrequest.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.categories.api.v2.CategoryMatches;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
@Builder
public class MissingMatchCategory {

  String categoryName;

  List<MatchAlert> matches;

  public CategoryMatches toCategoryMatches() {
    return CategoryMatches
        .newBuilder()
        .setCategories(categoryName)
        .addAllMatches(matches.stream().map(MatchAlert::getName).collect(
            toList()))
        .build();
  }

  public List<String> getMatchCategoryName() {
    return matches.stream().map(m -> m.getMatchCategoryName(categoryName)).collect(toList());
  }
}
