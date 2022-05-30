/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.data.MatchFeatureDao;

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

  public static MatchCategory from(MatchFeatureDao dao) {
    // TODO: I'm note sure if we should replace . ian / inside agent config to send via rabbit
    var match = new MatchCategory(dao.getAlertId(), dao.getMatchId(), dao.getCategory());

    match.updateCategoryValue(dao.getCategoryValue());

    return match;
  }

  public void updateCategoryValue(String categoryValue) {
    this.categoryValue = categoryValue;
  }

  public boolean hasValue() {
    return categoryValue != null;
  }
}
