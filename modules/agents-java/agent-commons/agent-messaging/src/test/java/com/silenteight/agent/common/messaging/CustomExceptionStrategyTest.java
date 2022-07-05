/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.agent.common.messaging;

import com.google.protobuf.InvalidProtocolBufferException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;
import javax.validation.ValidationException;

class CustomExceptionStrategyTest {

  @ParameterizedTest
  @MethodSource
  void isUserCauseFatal(Throwable throwable, boolean result) {
    //given
    var strategy = new CustomExceptionStrategy();

    //when
    var r = strategy.isUserCauseFatal(throwable);

    //then
    Assertions.assertThat(r).isEqualTo(result);
  }

  private static Stream<Arguments> isUserCauseFatal() {
    return Stream.of(
        Arguments.of(new IllegalArgumentException(), true),
        Arguments.of(new ValidationException(), true),
        Arguments.of(new InvalidProtocolBufferException("test"), true),
        Arguments.of(new Exception(), false)
    );
  }
}
