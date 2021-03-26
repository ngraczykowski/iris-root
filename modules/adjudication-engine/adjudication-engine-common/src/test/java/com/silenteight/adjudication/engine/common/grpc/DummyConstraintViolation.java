package com.silenteight.adjudication.engine.common.grpc;

import lombok.RequiredArgsConstructor;

import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;

@RequiredArgsConstructor
class DummyConstraintViolation<T> implements ConstraintViolation<T> {

  private final Path propertyPath;
  private final String message;

  static ConstraintViolation<?> make(String path, String message) {
    return new DummyConstraintViolation<>(
        PathImpl.createPathFromString(path),
        message);
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public String getMessageTemplate() {
    return null;
  }

  @Override
  public T getRootBean() {
    return null;
  }

  @Override
  public Class<T> getRootBeanClass() {
    return null;
  }

  @Override
  public Object getLeafBean() {
    return null;
  }

  @Override
  public Object[] getExecutableParameters() {
    return new Object[0];
  }

  @Override
  public Object getExecutableReturnValue() {
    return null;
  }

  @Override
  public Path getPropertyPath() {
    return propertyPath;
  }

  @Override
  public Object getInvalidValue() {
    return null;
  }

  @Override
  public ConstraintDescriptor<?> getConstraintDescriptor() {
    return null;
  }

  @Override
  public <U> U unwrap(Class<U> type) {
    return null;
  }
}
