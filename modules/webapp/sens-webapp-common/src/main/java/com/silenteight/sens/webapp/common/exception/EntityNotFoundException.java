package com.silenteight.sens.webapp.common.exception;

import java.util.Objects;
import javax.annotation.Nullable;

@SuppressWarnings({ "squid:S2176", "squid:MaximumInheritanceDepth" })
public class EntityNotFoundException extends javax.persistence.EntityNotFoundException {

  private static final long serialVersionUID = 7634945465313990336L;

  private static final String GENERIC_MESSAGE = "Entity not found";

  public EntityNotFoundException() {
    super(GENERIC_MESSAGE);
  }

  public <T> EntityNotFoundException(@Nullable T identifier) {
    super(GENERIC_MESSAGE + ": " + Objects.toString(identifier));
  }

  public <T> EntityNotFoundException(@Nullable T identifier, @Nullable Class<T> entityType) {
    super(GENERIC_MESSAGE + ": " + Objects.toString(identifier)
        + " (" + getEntityName(entityType) + ")");
  }

  private static <T> String getEntityName(@Nullable Class<T> entityType) {
    return entityType != null ? entityType.getSimpleName() : "<null>";
  }
}
