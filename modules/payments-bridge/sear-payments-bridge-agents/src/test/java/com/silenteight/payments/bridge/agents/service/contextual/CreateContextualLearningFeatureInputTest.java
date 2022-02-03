package com.silenteight.payments.bridge.agents.service.contextual;

import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsFeatureInput;
import com.silenteight.payments.bridge.agents.model.ContextualLearningAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateContextualLearningFeatureInputUseCase;
import com.silenteight.payments.bridge.common.app.LearningProperties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.payments.bridge.agents.service.contextual.ContextualLearningFixture.DISCRIMINATOR_PREFIX;
import static com.silenteight.payments.bridge.agents.service.contextual.ContextualLearningFixture.getDiscriminator;
import static com.silenteight.payments.bridge.agents.service.contextual.ContextualLearningFixture.getModelKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateContextualLearningFeatureInputTest {

  @Mock
  LearningProperties learningProperties;

  @Mock
  AlertedPartyIdContextAdapter alertedPartyIdContextAdapter;

  CreateContextualLearningFeatureInputUseCase createContextualLearningFeatureInput;

  @BeforeEach
  void beforeEach() {
    createContextualLearningFeatureInput =
        new CreateContextualLearningFeatureInput(alertedPartyIdContextAdapter, learningProperties);
  }

  @ParameterizedTest
  @CsvFileSource(
      resources = "/CreateContextualLearningFeatureInputUseCaseTestCases.csv",
      delimiter = ',',
      numLinesToSkip = 1)
  void parameterizedTest(
      String ofacId,
      String watchlistType,
      String matchingField,
      String matchText,
      String alertedPartyId) {

    when(alertedPartyIdContextAdapter.generateAlertedPartyId(matchingField, matchText)).thenReturn(
        alertedPartyId);
    when(learningProperties.getDiscriminatorPrefix()).thenReturn(DISCRIMINATOR_PREFIX);

    var actual = createContextualLearningFeatureInput.create(
        ContextualLearningAgentRequest.builder()
            .ofacId(ofacId)
            .watchlistType(watchlistType)
            .matchingField(matchingField)
            .matchText(matchText)
            .build());

    assertEquals(HistoricalDecisionsFeatureInput.newBuilder()
        .setFeature("features/contextualLearning")
        .setModelKey(getModelKey(ofacId, watchlistType, alertedPartyId))
        .setDiscriminator(getDiscriminator())
        .build(), actual);

  }

  @Test
  void throwNullPointerExceptionForNullArgument() {
    assertThrows(
        NullPointerException.class, () -> createContextualLearningFeatureInput.create(null));
  }
}
