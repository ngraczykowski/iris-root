package com.silenteight.adjudication.engine.analysis.recommendation;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertRecommendation;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.InsertRecommendationRequest;
import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.comments.comment.domain.FeatureContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toList;

public class RecommendationFixture {

  private static final ObjectMapper MAPPER = JsonConversionHelper.INSTANCE.objectMapper();

  public static AlertRecommendation createAlertRecommendation() {
    return AlertRecommendation.builder()
        .alertId(1)
        .analysisId(1)
        .recommendationId(1)
        .createdTime(Timestamp.valueOf("1983-05-24 12:34:56.0000"))
        .alertContext(createAlertContext())
        .matchIds(LongStream.range(1, 5).toArray())
        .build();
  }

  public static InsertRecommendationRequest createInsertRequest() {
    return InsertRecommendationRequest.builder()
        .alertId(1)
        .analysisId(1)
        .recommendationId(1)
        .recommendedAction("MATCH")
        .matchIds(new long[] { 11 })
        .matchContexts(new ObjectNode[] {
            MAPPER.convertValue(createMatchContext(), ObjectNode.class)
        })
        .build();
  }

  public static AlertContext createAlertContext() {
    var commentInput = new HashMap<String, Object>();
    commentInput.put("key", "value");
    return new AlertContext(
        "2137", commentInput, "recommended action",
        IntStream.range(1, 5).mapToObj(i -> createMatchContext()).collect(
            toList()));
  }

  static MatchContext createMatchContext() {
    var reason = new HashMap<String, Object>();
    reason.put("reason", "no_reason");

    var categories = new HashMap<String, String>();
    categories.put("key", "value");

    return MatchContext
        .builder()
        .matchId("123")
        .solution("NO_SOLUTION")
        .reason(reason)
        .categories(categories)
        .feature("features/dob", createFutureContext())
        .build();
  }

  static FeatureContext createFutureContext() {
    var reason = new HashMap<String, Object>();
    reason.put("reason", "no_reason");
    return FeatureContext
        .builder()
        .agentConfig("agents/dateAgent/versions/1.0.0/config/1")
        .solution("NO_SOLUTION")
        .reason(reason)
        .build();
  }
}
