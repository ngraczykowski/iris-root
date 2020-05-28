package com.silenteight.sep.base.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReflectionUtils {

  public static Optional<Object> invokeStaticGetter(Class<?> type, String methodName) {
    Method getterMethod = org.springframework.util.ReflectionUtils.findMethod(type, methodName);
    if (getterMethod == null)
      return Optional.empty();

    Object result = org.springframework.util.ReflectionUtils.invokeMethod(getterMethod, null);
    return Optional.ofNullable(result);
  }
}
