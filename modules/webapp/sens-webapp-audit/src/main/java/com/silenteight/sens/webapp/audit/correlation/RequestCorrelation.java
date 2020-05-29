package com.silenteight.sens.webapp.audit.correlation;

import java.util.UUID;

public class RequestCorrelation {

  private static final ThreadLocal<UUID> ID = ThreadLocal.withInitial(UUID::randomUUID);

  private RequestCorrelation() {
  }

  public static UUID id() {
    return ID.get();
  }

  public static void set(UUID id) {
    ID.set(id);
  }

  public static void remove() {
    ID.remove();
  }
}
