/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.data.CategoryAggregate;

import java.io.Serial;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class MatchCategory implements Serializable {

  @Serial private static final long serialVersionUID = 7217394902459244709L;
  private final long alertId;
  private final long matchId;
  private final String category;

  String categoryValue;

  public MatchCategory(long alertId, long matchId, CategoryAggregate categoryAggregate) {
    this.alertId = alertId;
    this.matchId = matchId;
    category = categoryAggregate.categoryName();
    categoryValue = categoryAggregate.categoryValue();
  }

  public void updateCategoryValue(String categoryValue) {
    this.categoryValue = categoryValue;
  }

  public boolean hasValue() {
    return categoryValue != null;
  }
}
