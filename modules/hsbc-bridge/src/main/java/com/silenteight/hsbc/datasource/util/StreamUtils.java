package com.silenteight.hsbc.datasource.util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class StreamUtils {

  @SafeVarargs
  public static <T> List<T> toDistinctList(Stream<T>... streams) {
    if (streams == null) {
      return emptyList();
    } else {
      return Stream.of(streams)
          .flatMap(Function.identity())
          .distinct().collect(toList());
    }
  }
}
