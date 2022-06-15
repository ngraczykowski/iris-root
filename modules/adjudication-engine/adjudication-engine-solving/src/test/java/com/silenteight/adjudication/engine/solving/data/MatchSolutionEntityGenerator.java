/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data;

import com.silenteight.adjudication.engine.solving.domain.MatchCategory;
import com.silenteight.adjudication.engine.solving.domain.MatchFeature;
import com.silenteight.adjudication.engine.solving.domain.MatchSolution;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class MatchSolutionEntityGenerator {

  public static MatchSolution createMatchSolution() {
    var feature = createFeature();
    var category = createCategory();
    return new MatchSolution(
        1L,
        1L,
        "clientId 1",
        "NO_DATA",
        "{\"reason\":\"something\"}",
        Map.of("features/geo2", feature),
        Map.of("category1", category,"category2", category));
  }

  @NotNull
  private static MatchCategory createCategory() {
    return new MatchCategory(1, 2,
        new CategoryAggregate("category1", "category Value"));
  }

  @NotNull
  private static MatchFeature createFeature() {
    return new MatchFeature(1, 11, "clientId",
        new FeatureAggregate(1, "features/geo2", "agents/geo/versions/1.0.0/configs/1",
            "CITY_MATCH",
            """
                {
                        "geoCompareResults": [
                          {
                            "type": "COUNTRY",
                            "alertedPartyValue": "MACEDONIA",
                            "watchlistPartyValue": "MACEDONIA"
                          },
                          {
                            "type": "LOCALITY",
                            "alertedPartyValue": "SKOPJE",
                            "watchlistPartyValue": "SKOPJE"
                          }
                        ]
                      }
                """));
  }

}
