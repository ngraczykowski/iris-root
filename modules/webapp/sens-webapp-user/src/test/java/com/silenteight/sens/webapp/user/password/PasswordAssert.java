package com.silenteight.sens.webapp.user.password;

import org.assertj.core.api.AbstractAssert;

import java.util.List;

import static com.google.common.collect.Lists.charactersOf;
import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.*;

class PasswordAssert extends AbstractAssert<PasswordAssert, String> {

  private PasswordAssert(String password) {
    super(password, PasswordAssert.class);
  }

  PasswordAssert hasAtLeastOneNumber() {
    assertThat(actual).containsPattern("\\d");

    return this;
  }

  PasswordAssert hasAtLeastOneLowercaseLetter() {
    assertThat(actual).containsPattern("[a-z]");

    return this;
  }

  PasswordAssert containsAllCharactersInAnyOrder(String characters) {
    List<Character> actualCharacters = newArrayList(charactersOf(actual));
    List<Character> checkedCharacters = charactersOf(characters);

    assertThat(checkedCharacters.stream().allMatch(actualCharacters::remove))
        .isTrue();

    return this;
  }

  static PasswordAssert assertThatPassword(String password) {
    return new PasswordAssert(password);
  }
}
