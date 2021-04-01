package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.hsbc.bridge.common.util.TimestampUtil;
import com.silenteight.hsbc.bridge.recommendation.RecommendationDto;
import com.silenteight.hsbc.bridge.recommendation.event.NewRecommendationEvent;

import com.google.protobuf.util.Timestamps;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.OffsetDateTime;

import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AdjudicationRecommendationListenerTest {

  @Mock
  private ApplicationEventPublisher applicationEventPublisher;

  @InjectMocks
  private AdjudicationRecommendationListener underTest;

  @Test
  void givenRecommendationIsReceived_publishesCorrectEvent() {
    underTest.onRecommendation(Fixtures.RECOMMENDATION_1.asRecommendation());

    then(applicationEventPublisher).should()
        .publishEvent(Fixtures.RECOMMENDATION_1.asRecommendationEvent());
  }

  @Test
  void givenMultipleRecommendationsReceived_publishesCorrectEvents() {
    underTest.onRecommendation(Fixtures.RECOMMENDATION_1.asRecommendation());
    underTest.onRecommendation(Fixtures.RECOMMENDATION_2.asRecommendation());

    then(applicationEventPublisher)
        .should()
        .publishEvent(Fixtures.RECOMMENDATION_1.asRecommendationEvent());
    then(applicationEventPublisher)
        .should()
        .publishEvent(Fixtures.RECOMMENDATION_2.asRecommendationEvent());

  }

  private static class Fixtures {

    static final TestRecommendation RECOMMENDATION_1 = new TestRecommendation(
        Recommendation.newBuilder()
            .setAlert("SomeAlertId")
            .setName("SomeRecommendationName")
            .setRecommendationComment("SomeRecommendationComment")
            .setRecommendedAction("SomeAction")
            .setCreateTime(Timestamps.fromNanos(OffsetDateTime.MIN.getNano()))
            .build()
    );

    static final TestRecommendation RECOMMENDATION_2 = new TestRecommendation(
        Recommendation.newBuilder()
            .setAlert("SomeAlertId2")
            .setName("SomeRecommendationName2")
            .setRecommendationComment("SomeRecommendationComment2")
            .setRecommendedAction("SomeAction2")
            .setCreateTime(Timestamps.fromNanos(OffsetDateTime.MAX.getNano()))
            .build()
    );
  }

  @RequiredArgsConstructor
  private static class TestRecommendation {

    private final Recommendation recommendation;

    Recommendation asRecommendation() {
      return recommendation;
    }

    RecommendationDto asRecommendationDto() {
      return RecommendationDto.builder()
          .name(recommendation.getName())
          .alert(recommendation.getAlert())
          .recommendationComment(recommendation.getRecommendationComment())
          .recommendedAction(recommendation.getRecommendedAction())
          .date(TimestampUtil.toOffsetDateTime(recommendation.getCreateTime()))
          .build();
    }

    NewRecommendationEvent asRecommendationEvent() {
      return new NewRecommendationEvent(asRecommendationDto());
    }
  }
}
