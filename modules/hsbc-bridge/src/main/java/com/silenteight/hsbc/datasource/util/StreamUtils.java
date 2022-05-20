package com.silenteight.hsbc.datasource.util;

import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class StreamUtils {

  @SafeVarargs
  public static <T> List<T> toDistinctList(Stream<T>... streams) {
    if (streams == null) {
      return Collections.emptyList();
    } else {
      return Stream.of(streams)
          .flatMap(Function.identity())
          .distinct().collect(Collectors.toList());
    }
  }
}
