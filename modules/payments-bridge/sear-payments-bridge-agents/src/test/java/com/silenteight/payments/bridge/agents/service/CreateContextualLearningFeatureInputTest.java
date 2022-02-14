package com.silenteight.payments.bridge.agents.service;

import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsFeatureInput;
import com.silenteight.payments.bridge.agents.model.ContextualLearningAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateContextualLearningFeatureInputUseCase;
import com.silenteight.payments.bridge.common.agents.ContextualAlertedPartyIdModel;
import com.silenteight.payments.bridge.common.agents.ContextualLearningProperties;
import com.silenteight.payments.bridge.common.app.LearningProperties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.payments.bridge.agents.service.ContextualLearningFixture.DISCRIMINATOR_PREFIX;
import static com.silenteight.payments.bridge.agents.service.ContextualLearningFixture.getDiscriminator;
import static com.silenteight.payments.bridge.agents.service.ContextualLearningFixture.getModelKey;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_DISC_TP;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_FEATURE_NAME_TP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateContextualLearningFeatureInputTest {

  @Mock
  LearningProperties learningProperties;

  @Mock
  ContextualLearningProperties contextualLearningProperties;

  CreateContextualLearningFeatureInputUseCase createContextualLearningFeatureInput;

  @BeforeEach
  void beforeEach() {
    createContextualLearningFeatureInput =
        new CreateContextualLearningFeatureInput(learningProperties, contextualLearningProperties);
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
      int numberOfTokensLeft,
      int numberOfTokensRight,
      int minTokens,
      boolean lineBreaks,
      String alertedPartyId) {

    when(learningProperties.getDiscriminatorPrefix()).thenReturn(DISCRIMINATOR_PREFIX);
    when(contextualLearningProperties.getModelBuilder()).thenReturn(
        ContextualAlertedPartyIdModel.builder()
            .numberOfTokensLeft(numberOfTokensLeft)
            .numberOfTokensRight(numberOfTokensRight)
            .minTokens(minTokens)
            .lineBreaks(lineBreaks));

    var actual = createContextualLearningFeatureInput.create(
        ContextualLearningAgentRequest.builder()
            .ofacId(ofacId)
            .watchlistType(watchlistType)
            .matchingField(replaceSpecialCharacters(matchingField))
            .matchText(matchText)
            .feature(CONTEXTUAL_LEARNING_FEATURE_NAME_TP)
            .discriminator(CONTEXTUAL_LEARNING_DISC_TP)
            .build());

    assertEquals(HistoricalDecisionsFeatureInput.newBuilder()
        .setFeature(CONTEXTUAL_LEARNING_FEATURE_NAME_TP)
        .setModelKey(getModelKey(ofacId, watchlistType, replaceSpecialCharacters(alertedPartyId)))
        .setDiscriminator(getDiscriminator())
        .build(), actual);

  }

  private static String replaceSpecialCharacters(String string) {
    return string.replace("\\n", "\n").replace("\\r", "\r");
  }

  @Test
  void throwNullPointerExceptionForNullArgument() {
    assertThrows(
        NullPointerException.class, () -> createContextualLearningFeatureInput.create(null));
  }
}
