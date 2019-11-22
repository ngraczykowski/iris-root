package com.silenteight.sens.webapp.users.user.exception;

import com.silenteight.sens.webapp.users.user.ModifiableUserField;

public class InvalidUserModificationFieldException extends RuntimeException {

  private static final long serialVersionUID = 8901689133752333882L;

  public InvalidUserModificationFieldException(ModifiableUserField field) {
    super(String.format("Not supported User modification field: '%s'", field.name()));
  }
}
