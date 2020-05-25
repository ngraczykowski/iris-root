package com.silenteight.serp.aspects.validation;

import lombok.Getter;

import org.aspectj.lang.reflect.MethodSignature;

import javax.annotation.Nullable;

public class UnsupportedStaticMethodException extends RuntimeException {

  private static final long serialVersionUID = 3415181379665390279L;

  @Getter
  private final String method;

  UnsupportedStaticMethodException(@Nullable MethodSignature methodSignature) {
    super("Validation of static methods is not supported by Bean Validation.");

    method = methodSignature != null ? methodSignature.toLongString() : null;
  }

  @Override
  public String toString() {
    String className = getClass().getName();
    String methodName = (method != null) ? ("[" + method + "]") : "";
    String message = getLocalizedMessage();

    return (message != null)
           ? (className + ": " + methodName + " " + message)
           : (className + ": " + methodName);
  }
}
