package com.silenteight.hsbc.bridge.adjudication;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.hsbc.bridge.analysis.AnalysisServiceApi;
import com.silenteight.hsbc.bridge.analysis.dto.GetRecommendationsDto;
import com.silenteight.hsbc.bridge.recommendation.RecommendationDto;
import com.silenteight.hsbc.bridge.recommendation.event.NewRecommendationEvent;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AdjudicationRecommendationListenerTest {

  @Mock
  private ApplicationEventPublisher applicationEventPublisher;

  @Mock
  private AnalysisServiceApi analysisServiceApi;

  @InjectMocks
  private AdjudicationRecommendationListener underTest;


  @Test
  void givenRecommendationsGeneratedReceivedWithOneElement_publishCorrectEvents() {
    RecommendationsGenerated recommendationsGenerated = RECOMMENDATIONS_GENERATED_1;
    String analysisString = recommendationsGenerated.getAnalysis();
    var analysis = GetRecommendationsDto.builder().analysis(analysisString).build();

    given(analysisServiceApi.getRecommendations(analysis)).willReturn(
            List.of(RECOMMENDATION_INFO_1.asRecommendationDto())
    );

    underTest.onRecommendation(recommendationsGenerated);

    then(applicationEventPublisher)
            .should()
            .publishEvent(RECOMMENDATION_INFO_1.asRecommendationEvent());
  }

  @Test
  void givenRecommendationsGeneratedReceivedWithManyElements_publishCorrectEvents() {
    RecommendationsGenerated recommendationsGenerated = RECOMMENDATIONS_GENERATED_2;
    String analysisString = recommendationsGenerated.getAnalysis();
    var analysis = GetRecommendationsDto.builder().analysis(analysisString).build();

    given(analysisServiceApi.getRecommendations(analysis)).willReturn(
            List.of(RECOMMENDATION_INFO_1.asRecommendationDto(), RECOMMENDATION_INFO_2.asRecommendationDto())
    );

    underTest.onRecommendation(recommendationsGenerated);

    then(applicationEventPublisher)
            .should()
            .publishEvent(RECOMMENDATION_INFO_1.asRecommendationEvent());

    then(applicationEventPublisher)
            .should()
            .publishEvent(RECOMMENDATION_INFO_2.asRecommendationEvent());
  }


  static final TestRecommendationInfo RECOMMENDATION_INFO_1 = new TestRecommendationInfo(
          RecommendationsGenerated.RecommendationInfo.newBuilder()
                  .setAlert("Alert_1")
                  .setRecommendation("Recommendation_name_1")
                  .build()
  );

  static final TestRecommendationInfo RECOMMENDATION_INFO_2 = new TestRecommendationInfo(
          RecommendationsGenerated.RecommendationInfo.newBuilder()
                  .setAlert("Alert_2")
                  .setRecommendation("Recommendation_name_2")
                  .build()
  );


  static final RecommendationsGenerated RECOMMENDATIONS_GENERATED_1 = RecommendationsGenerated
          .newBuilder()
          .setAnalysis("SomeAnalysis")
          .addAllRecommendationInfos(Collections.singleton(RECOMMENDATION_INFO_1.asRecommendationInfo()))
          .build();

  static final RecommendationsGenerated RECOMMENDATIONS_GENERATED_2 = RecommendationsGenerated
          .newBuilder()
          .setAnalysis("SomeAnalysis2")
          .addAllRecommendationInfos(List.of(RECOMMENDATION_INFO_1.asRecommendationInfo(), RECOMMENDATION_INFO_2.asRecommendationInfo()))
          .build();


  @RequiredArgsConstructor
  private static class TestRecommendationInfo {
    private final RecommendationsGenerated.RecommendationInfo recommendation;

    RecommendationsGenerated.RecommendationInfo asRecommendationInfo() {
      return recommendation;
    }

    RecommendationDto asRecommendationDto() {
      return RecommendationDto.builder()
              .name(recommendation.getRecommendation())
              .alert(recommendation.getAlert())
              .recommendationComment("SomeComment")
              .recommendedAction("SomeAction")
              .date(OffsetDateTime.MAX)
              .build();
    }

    NewRecommendationEvent asRecommendationEvent() {
      return new NewRecommendationEvent(asRecommendationDto());
    }
  }

}
