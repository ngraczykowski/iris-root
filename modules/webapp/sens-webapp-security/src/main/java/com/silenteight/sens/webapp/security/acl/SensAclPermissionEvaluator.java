package com.silenteight.sens.webapp.security.acl;

import com.silenteight.sens.webapp.kernel.security.SensUserDetails;

import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

public class SensAclPermissionEvaluator extends AclPermissionEvaluator {

  public SensAclPermissionEvaluator(AclService aclService) {
    super(aclService);
  }

  @Override
  public boolean hasPermission(
      Authentication authentication, Object domainObject, Object permission) {

    if (isSuperUser(authentication))
      return true;
    else
      return super.hasPermission(authentication, domainObject, permission);
  }

  @Override
  public boolean hasPermission(
      Authentication authentication, Serializable targetId, String targetType, Object permission) {

    if (isSuperUser(authentication))
      return true;
    else
      return super.hasPermission(authentication, targetId, targetType, permission);
  }

  private static boolean isSuperUser(Authentication authentication) {
    if (authentication.getPrincipal() instanceof SensUserDetails)
      return ((SensUserDetails) authentication.getPrincipal()).isSuperUser();
    else
      return false;
  }
}
