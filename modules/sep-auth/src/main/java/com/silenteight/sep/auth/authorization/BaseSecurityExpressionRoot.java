package com.silenteight.sep.auth.authorization;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

abstract class BaseSecurityExpressionRoot
    extends SecurityExpressionRoot
    implements MethodSecurityExpressionOperations {

  private Object filterObject;
  private Object returnObject;
  private Object targetObject;

  public BaseSecurityExpressionRoot(Authentication authentication) {
    super(authentication);
  }

  @Override
  public void setFilterObject(Object filterObject) {
    this.filterObject = filterObject;
  }

  @Override
  public Object getFilterObject() {
    return filterObject;
  }

  @Override
  public void setReturnObject(Object returnObject) {
    this.returnObject = returnObject;
  }

  @Override
  public Object getReturnObject() {
    return returnObject;
  }

  @Override
  public Object getThis() {
    return targetObject;
  }

  void setThis(Object targetObject) {
    this.targetObject = targetObject;
  }

  public abstract boolean isAuthorized(String method);
}
