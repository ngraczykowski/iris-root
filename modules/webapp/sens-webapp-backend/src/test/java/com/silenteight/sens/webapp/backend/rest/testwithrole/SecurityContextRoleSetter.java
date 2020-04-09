package com.silenteight.sens.webapp.backend.rest.testwithrole;

import java.util.List;

public interface SecurityContextRoleSetter {

  void setRoles(List<String> roles);
}
