package com.silenteight.connector.ftcc.callback;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.recommendation.api.library.v1.AlertOut;
import com.silenteight.recommendation.api.library.v1.RecommendationOut;
import com.silenteight.recommendation.api.library.v1.RecommendationsOut;

import java.util.stream.IntStream;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CallbackFixtures {

  public static RecommendationsOut createRecommendationsOut(int number) {
    return RecommendationsOut
        .builder()
        .recommendations(
            IntStream.range(0, number)
                .mapToObj(alertId -> RecommendationOut
                    .builder()
                    .recommendationComment("PTP")
                    .alert(AlertOut.builder().id("messages/" + randomUUID()).build())
                    .build())
                .collect(toList()))
        .build();
  }
}
