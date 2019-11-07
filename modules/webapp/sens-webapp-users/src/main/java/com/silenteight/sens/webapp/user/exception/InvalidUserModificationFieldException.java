package com.silenteight.sens.webapp.user.exception;

import com.silenteight.sens.webapp.user.ModifiableUserField;

public class InvalidUserModificationFieldException extends RuntimeException {

  private static final long serialVersionUID = 8901689133752333882L;

  public InvalidUserModificationFieldException(ModifiableUserField field) {
    super(String.format("Not supported User modification field: '%s'", field.name()));
  }
}
