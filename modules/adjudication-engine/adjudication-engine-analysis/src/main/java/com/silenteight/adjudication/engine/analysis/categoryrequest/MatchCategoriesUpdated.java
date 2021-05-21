package com.silenteight.adjudication.engine.analysis.categoryrequest;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MatchCategoriesUpdated {

  private final List<String> analysis;
}
