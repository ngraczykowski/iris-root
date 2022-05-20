package com.silenteight.hsbc.bridge.util;

import lombok.experimental.UtilityClass;

import com.google.common.collect.Sets;

import java.util.function.Function;
import java.util.function.Predicate;

@UtilityClass
public class StreamUtils {

  public static <T> Predicate<T> distinctBy(Function<? super T, ?> function) {
    var objects = Sets.newConcurrentHashSet();
    return t -> objects.add(function.apply(t));
  }
}
