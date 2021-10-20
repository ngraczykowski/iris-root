package com.silenteight.payments.bridge.event;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
public class DomainEvent {

  private final Map<Class<?>, Supplier<?>> dataSuppliers = new HashMap<>();

  @SuppressWarnings("unchecked")
  public <T> T getData(Class<T> type) {
    if (!dataSuppliers.containsKey(type)) {
      throw new IllegalArgumentException(
          String.format("Requested type %s has no register provider", type.getName()));
    }

    var supplier = dataSuppliers.get(type);
    Object result;
    synchronized (this) {
      result = supplier.get();
    }
    return (T) result;
  }

  public void registerCollector(Class<?> type, Supplier<?> supplier) {
    dataSuppliers.put(type, supplier);
  }
}
