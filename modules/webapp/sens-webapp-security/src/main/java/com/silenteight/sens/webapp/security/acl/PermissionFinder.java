package com.silenteight.sens.webapp.security.acl;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.kernel.security.SensPermission;
import com.silenteight.sens.webapp.security.acl.dto.PermissionQuery;
import com.silenteight.sens.webapp.security.acl.dto.PermissionView;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
public class PermissionFinder {

  private final PermissionRepository repository;

  private final PermissionEvaluator permissionEvaluator;

  public List<PermissionView> findAll(PermissionQuery permissionQuery) {
    return null;
  }

  public Set<SensPermission> getPermissionsForObject(Object domainObject) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return Stream
        .of(SensPermission.values())
        .filter(perm -> permissionEvaluator.hasPermission(authentication, domainObject, perm.name()))
        .collect(toSet());
  }
}
