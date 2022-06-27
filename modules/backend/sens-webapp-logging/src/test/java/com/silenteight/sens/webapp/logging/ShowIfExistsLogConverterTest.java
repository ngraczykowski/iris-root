package com.silenteight.sens.webapp.logging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ShowIfExistsLogConverterTest {

  public static final String VALID_INPUT = "someInput";
  public static final String PATTERN = "somePattern";
  public static final String PATTERN_WITH_REPLACEMENT = "[$]";
  @Spy
  private ShowIfExistsLogConverter underTest;

  @Test
  void firstOptionIsEmpty_returnsEmpty() {
    given(underTest.getFirstOption()).willReturn("");

    String actual = underTest.transform(null, VALID_INPUT);

    assertThat(actual).isEmpty();
  }

  @Test
  void firstOptionIsNull_returnsEmpty() {
    given(underTest.getFirstOption()).willReturn(null);

    String actual = underTest.transform(null, VALID_INPUT);

    assertThat(actual).isEmpty();
  }

  @Test
  void inputIsEmpty_returnsEmpty() {
    given(underTest.getFirstOption()).willReturn(PATTERN);

    String actual = underTest.transform(null, "");

    assertThat(actual).isEmpty();
  }

  @Test
  void inputIsNull_returnsEmpty() {
    given(underTest.getFirstOption()).willReturn(PATTERN);

    String actual = underTest.transform(null, null);

    assertThat(actual).isEmpty();
  }

  @Test
  void patternWithCorrectReplacementSign_returnsCorrect() {
    given(underTest.getFirstOption()).willReturn(PATTERN_WITH_REPLACEMENT);

    String actual = underTest.transform(null, VALID_INPUT);

    assertThat(actual).isEqualTo("[" + VALID_INPUT + "]");
  }

  @Test
  void patternWithoutReplacementSign_returnsPattern() {
    given(underTest.getFirstOption()).willReturn(PATTERN);

    String actual = underTest.transform(null, VALID_INPUT);

    assertThat(actual).isEqualTo(PATTERN);
  }
}
