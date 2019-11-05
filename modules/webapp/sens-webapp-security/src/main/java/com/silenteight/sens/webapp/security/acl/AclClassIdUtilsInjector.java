package com.silenteight.sens.webapp.security.acl;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class AclClassIdUtilsInjector {

  private static final String CLASS_NAME = "org.springframework.security.acls.jdbc.AclClassIdUtils";
  private static final Class<?> CLASS;
  private static final Method CONVERSION_SERVICE_SETTER;

  private final Object aclClassIdUtils;

  static {
    try {
      CLASS = ClassUtils.forName(CLASS_NAME, null);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException(e);
    }

    CONVERSION_SERVICE_SETTER = initializeSetter();
  }

  private static Method initializeSetter() {
    Method setter = ReflectionUtils.findMethod(
        CLASS, "setConversionService", ConversionService.class);

    if (setter == null)
      throw new FailedToSetConversionServiceException();

    ReflectionUtils.makeAccessible(setter);

    return setter;
  }

  AclClassIdUtilsInjector() {
    aclClassIdUtils = createInstance();
    setConversionService(new GenericConversionService());
  }

  private static Object createInstance() {
    try {
      Constructor<?> constructor = ReflectionUtils.accessibleConstructor(CLASS);
      return constructor.newInstance();
    } catch (ReflectiveOperationException e) {
      throw new FailedToCreateObjectException(e);
    }
  }

  final void setConversionService(ConversionService conversionService) {
    ReflectionUtils.invokeMethod(CONVERSION_SERVICE_SETTER, aclClassIdUtils, conversionService);
  }

  void injectTo(Object targetObject) {
    injectTo(targetObject, "setAclClassIdUtils");
  }

  void injectTo(Object targetObject, String setterName) {
    Class<?> targetClass = targetObject.getClass();

    Method m = ReflectionUtils.findMethod(targetClass, setterName, CLASS);

    if (m == null)
      throw new FailedToInjectAclClassIdUtilsException();

    ReflectionUtils.invokeMethod(m, targetObject, aclClassIdUtils);
  }

  static final class FailedToCreateObjectException extends RuntimeException {

    private static final long serialVersionUID = -9168630115227888888L;

    FailedToCreateObjectException(Throwable cause) {
      super("Failed to create object", cause);
    }
  }

  static final class FailedToSetConversionServiceException extends RuntimeException {

    private static final long serialVersionUID = -7381052691683532423L;

    private static final String MESSAGE = "Failed to set conversion service";

    FailedToSetConversionServiceException() {
      super(MESSAGE);
    }
  }

  static final class FailedToInjectAclClassIdUtilsException extends RuntimeException {

    private static final long serialVersionUID = 1848756870405438700L;

    FailedToInjectAclClassIdUtilsException() {
      super("Failed to inject AclClassIdUtils object");
    }
  }
}
