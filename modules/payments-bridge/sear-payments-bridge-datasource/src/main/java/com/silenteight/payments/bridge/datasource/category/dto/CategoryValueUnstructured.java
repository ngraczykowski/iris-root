package com.silenteight.payments.bridge.datasource.category.dto;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;

@Value
@Builder
public class CategoryValueUnstructured {

  String matchName;

  String alertName;

  String tag;

  SolutionType solutionType;

  WatchlistType watchlistType;

  String allMatchingFieldValues;

}
