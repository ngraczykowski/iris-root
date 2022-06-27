package com.silenteight.sens.webapp.user.password;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


import com.silenteight.sep.usermanagement.api.credentials.TemporaryPasswordGenerator;
import com.silenteight.sep.usermanagement.api.credentials.dto.TemporaryPassword;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static com.google.common.collect.Lists.charactersOf;
import static com.google.common.collect.Lists.newArrayList;

@RequiredArgsConstructor
public class SensCompatiblePasswordGenerator implements TemporaryPasswordGenerator {

  private final SensPasswordGeneratorConfig config;

  private final StringGenerator alphabetic;
  private final StringGenerator alphanumeric;
  private final StringGenerator numeric;

  @Override
  public TemporaryPassword generate() {
    String requiredLowerLetter =
        alphabetic.generate(config.getLowercaseLettersCount()).toLowerCase();
    String requiredNumber = numeric.generate(config.getNumbersCount());
    String base = alphanumeric.generate(config.getBaseCharsCount());

    List<Character> password = newArrayList(ImmutableList.<Character>builder()
        .addAll(charactersOf(base))
        .addAll(charactersOf(requiredLowerLetter))
        .addAll(charactersOf(requiredNumber))
        .build());

    Collections.shuffle(password);

    return TemporaryPassword.of(Joiner.on("").join(password));
  }

  @Getter
  @ToString
  static final class SensPasswordGeneratorConfig {

    SensPasswordGeneratorConfig(int passwordLength, int numbersCount, int lowercaseLettersCount) {
      if (passwordLength < 1)
        throw new IllegalArgumentException("Password length must be positive.");

      if (numbersCount < 0 && lowercaseLettersCount < 0)
        throw new IllegalArgumentException("Number or lower letters count can't be negative.");

      if ((numbersCount + lowercaseLettersCount) > passwordLength)
        throw new IllegalArgumentException("Password length must be greater or equal"
            + " than sum of numbers and lower case letters.");

      this.passwordLength = passwordLength;
      this.numbersCount = numbersCount;
      this.lowercaseLettersCount = lowercaseLettersCount;
    }

    private final int passwordLength;
    private final int numbersCount;
    private final int lowercaseLettersCount;

    int getBaseCharsCount() {
      return passwordLength - numbersCount - lowercaseLettersCount;
    }
  }

  @FunctionalInterface
  interface StringGenerator extends Function<Integer, String> {

    @Override
    default String apply(Integer integer) {
      return generate(integer);
    }

    String generate(Integer length);
  }
}
