package com.silenteight.serp.governance.common.signature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CanonicalFeatureVectorFactoryTest {

  private static final String NAME_AGENT = "nameAgent";
  private static final String DOB_AGENT = "dobAgent";
  private static final String RESIDENCY_AGENT = "residencyAgent";
  private static final String PERFECT_MATCH = "PERFECT_MATCH";
  private static final String NO_DATA = "NO_DATA";
  private static final String EXACT = "EXACT";
  private static final List<String> AGENT_NAME_LIST_WITH_THE_SAME_VALUE =
      of(NAME_AGENT, NAME_AGENT);
  private static final List<String> AGENT_NAME_LIST_WITH_ONE_BLANK_VALUE = of(NAME_AGENT, " ");
  private static final List<String> CORRECT_AGENT_NAME_LIST_WITH_TWO_NAMES =
      of(NAME_AGENT, DOB_AGENT);
  private static final List<String> EMPTY_AGENT_NAME_LIST = of();
  private static final List<String> CORRECT_AGENT_NAME_LIST_WITH_THREE_NAMES =
      of(NAME_AGENT, DOB_AGENT, RESIDENCY_AGENT);
  private static final List<String> CORRECT_FEATURE_VALUES_LIST_WITH_THREE_VALUES =
      of(PERFECT_MATCH, EXACT, NO_DATA);
  private static final List<String> FEATURE_VALUES_LIST_WITH_ONE_BLANK_VALUE =
      of(PERFECT_MATCH, " ", NO_DATA);
  private static final List<String> EMPTY_FEATURE_VALUE_LIST = of();

  private SignatureCalculator signatureCalculator;
  private CanonicalFeatureVectorFactory underTest;

  @BeforeEach
  void setUp() {
    signatureCalculator = mock(SignatureCalculator.class);
    underTest = new CanonicalFeatureVectorFactory(signatureCalculator);
  }

  @Test
  void shouldThrowExceptionWithCorrectMessageWhenDuplicatedAgentsName() {
    assertThatThrownBy(
        () -> underTest.fromNamesAndValues(
            AGENT_NAME_LIST_WITH_THE_SAME_VALUE,
            CORRECT_FEATURE_VALUES_LIST_WITH_THREE_VALUES))
        .isInstanceOf(
            InvalidInputException.class)
        .hasMessage("Duplicated agents names");
  }

  @Test
  void shouldThrowExceptionWithCorrectMessageWhenMismatchBetweenNameCountAndFeatureValue() {
    assertThatThrownBy(
        () -> underTest.fromNamesAndValues(
            CORRECT_AGENT_NAME_LIST_WITH_TWO_NAMES,
            CORRECT_FEATURE_VALUES_LIST_WITH_THREE_VALUES))
        .isInstanceOf(
            InvalidInputException.class)
        .hasMessage("Mismatch between agents name count and feature vectors count");
  }

  @Test
  void shouldThrowExceptionWhenEmptyFeatureNameField() {
    assertThatThrownBy(() -> underTest.fromNamesAndValues(
        AGENT_NAME_LIST_WITH_ONE_BLANK_VALUE,
        CORRECT_FEATURE_VALUES_LIST_WITH_THREE_VALUES))
        .isInstanceOf(
            InvalidInputException.class)
        .hasMessage("Some fields are empty");
  }

  @Test
  void shouldThrowExceptionWhenEmptyFeatureValueField() {
    assertThatThrownBy(() -> underTest.fromNamesAndValues(
        CORRECT_AGENT_NAME_LIST_WITH_TWO_NAMES,
        FEATURE_VALUES_LIST_WITH_ONE_BLANK_VALUE))
        .isInstanceOf(
            InvalidInputException.class)
        .hasMessage("Some fields are empty");
  }

  @Test
  void shouldThrowExceptionWhenNoDataInInput() {
    assertThatThrownBy(
        () -> underTest.fromNamesAndValues(EMPTY_AGENT_NAME_LIST, EMPTY_FEATURE_VALUE_LIST))
        .isInstanceOf(
            InvalidInputException.class)
        .hasMessage("Missing data");
  }

  @Test
  void shouldNotThrowInvalidExceptionWhenInputIsValid() {
    try {
      underTest.fromNamesAndValues(
          CORRECT_AGENT_NAME_LIST_WITH_THREE_NAMES,
          CORRECT_AGENT_NAME_LIST_WITH_THREE_NAMES);
    } catch (Exception e) {
      fail("Should not have thrown any exception");
    }
  }
}
