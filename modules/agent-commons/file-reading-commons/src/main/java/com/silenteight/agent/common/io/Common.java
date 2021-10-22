package com.silenteight.agent.common.io;

import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.function.Function;

import static com.silenteight.agent.common.io.FileFormatConstants.VALUES_SEPARATOR;

@UtilityClass
class Common {

  static Function<String[], String> extractSingleKey() {
    return split -> split[0];
  }

  static Function<String[], Set<String>> extractMultipleKeys() {
    return split -> Set.of(split[0].split(VALUES_SEPARATOR));
  }

  static Function<String[], Set<String>> extractMultipleValues() {
    return split -> Set.of(split[1].split(VALUES_SEPARATOR));
  }
}
