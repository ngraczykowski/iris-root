package com.silenteight.serp.common.testing.transaction;

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.transaction.aspectj.AbstractTransactionAspect;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CleanTransactionManagerCacheExtension implements BeforeTestExecutionCallback {

  @Override
  public void beforeTestExecution(ExtensionContext context) {
    cleanTransactionManagerCache();
  }

  public void cleanTransactionManagerCache() {
    try {
      Method destroy = AbstractTransactionAspect.class.getMethod("destroy");
      destroy.invoke(AnnotationTransactionAspect.aspectOf());
    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      // do nothing
    }
  }
}
