package com.silenteight.hsbc.bridge.util;

import lombok.experimental.UtilityClass;

import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.collect.Sets.newConcurrentHashSet;

@UtilityClass
public class StreamUtils {

  public static <T> Predicate<T> distinctBy(Function<? super T, ?> function) {
    var objects = newConcurrentHashSet();
    return t -> objects.add(function.apply(t));
  }
}
