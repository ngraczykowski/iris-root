package com.silenteight.sep.auth.authorization;

import java.util.Map;
import java.util.Set;

interface PermissionsByRoleProvider {

  Map<String, Set<String>> provide();
}
