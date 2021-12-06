package com.silenteight.autoconfigure.protobuf;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ReflectionUtils {

  public static Optional<Object> invokeStaticGetter(Class<?> type, String methodName) {
    var getterMethod = org.springframework.util.ReflectionUtils.findMethod(type, methodName);
    if (getterMethod == null)
      return Optional.empty();

    var result = org.springframework.util.ReflectionUtils.invokeMethod(getterMethod, null);
    return Optional.ofNullable(result);
  }
}
