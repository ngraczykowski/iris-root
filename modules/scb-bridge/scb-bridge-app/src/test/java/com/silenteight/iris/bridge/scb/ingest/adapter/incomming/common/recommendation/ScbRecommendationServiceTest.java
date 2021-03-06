/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.recommendation;

import com.silenteight.iris.bridge.scb.ingest.domain.model.AlertMetadata;
import com.silenteight.iris.bridge.scb.ingest.domain.payload.PayloadConverter;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.Alert;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.Recommendation;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.RecommendedAction;
import com.silenteight.sep.base.testing.transaction.RunWithoutTransactionManager;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWithoutTransactionManager
class ScbRecommendationServiceTest {

  private static final String SYSTEM_ID = "systemId";
  private static final String BATCH_ID = "batchId";
  private static final String DISCRIMINATOR = "321";
  private static final String PAYLOAD = "payload";

  @Mock
  private ScbRecommendationRepository scbRecommendationRepository;

  @Mock
  private ScbDiscriminatorMatcher scbDiscriminatorMatcher;

  @Mock
  private PayloadConverter payloadConverter;

  private Fixtures fixtures = new Fixtures();
  private ScbRecommendationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new ScbRecommendationService(
        scbRecommendationRepository,
        scbDiscriminatorMatcher,
        payloadConverter);
  }

  @Test
  void shouldStoreToDatabaseAndCheckIfRecommendationAlreadyExists() {
    //given
    var alertMetadata = new AlertMetadata(null, DISCRIMINATOR, SYSTEM_ID, BATCH_ID);
    when(payloadConverter.deserializeFromJsonToObject(PAYLOAD, AlertMetadata.class))
        .thenReturn(alertMetadata);
    var alertRecommendation = fixtures.recommendation;
    var alertId = alertRecommendation.alert().id();
    when(scbDiscriminatorMatcher.match(anyString(), anyString())).thenReturn(true);

    var scbRecommendation = new ScbRecommendation();
    scbRecommendation.setSystemId(SYSTEM_ID);
    scbRecommendation.setDiscriminator("");

    // when
    underTest.saveRecommendation(alertRecommendation);
    Optional<ScbRecommendation> result =
        underTest.getRecommendation(SYSTEM_ID, "", List.of(scbRecommendation));

    //then
    Assertions.assertThat(result).isPresent();
  }

  private static class Fixtures {

    private static final OffsetDateTime TIME_1 = OffsetDateTime.now();

    Recommendation recommendation = createRecommendation("comment_1", TIME_1);

    @Nonnull
    private static Recommendation createRecommendation(String comment, OffsetDateTime time) {
      return Recommendation.builder()
          .recommendedComment(comment)
          .recommendedAction(RecommendedAction.ACTION_FALSE_POSITIVE)
          .recommendedAt(time)
          .alert(Alert.builder()
              .id(SYSTEM_ID)
              .metadata(PAYLOAD)
              .build())
          .build();
    }
  }

}
