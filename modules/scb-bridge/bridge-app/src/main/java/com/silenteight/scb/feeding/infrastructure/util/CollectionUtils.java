package com.silenteight.scb.feeding.infrastructure.util;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class CollectionUtils {
  public <T> List<T> addAllIfNotNull(List<T> result, Collection<T> source) {
    if (source != null) {
      result.addAll(source);
    }
    return result;
  }

  public <T> List<T> addIfNotNull(List<T> result, T source) {
    if (source != null) {
      result.add(source);
    }
    return result;
  }
}
