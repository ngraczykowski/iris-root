package com.silenteight.sens.webapp.security.acl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.kernel.security.SensPermission;
import com.silenteight.sens.webapp.security.acl.dto.AccessListRequest;
import com.silenteight.sens.webapp.security.acl.dto.UserPermissionRequest;
import com.silenteight.sens.webapp.security.acl.exception.AccessListNotFoundException;
import com.silenteight.sens.webapp.security.acl.exception.UserPermissionAlreadyGrantedException;

import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
@Slf4j
public class AccessManagementService {

  @NonNull
  private final ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy;

  @NonNull
  private final MutableAclService aclService;

  @NonNull
  private final PermissionFactory permissionFactory;

  @Transactional
  public void createAccessList(AccessListRequest request) {
    ObjectIdentity identity = getIdentity(request.getDomainObject());

    log.info("Creating access list: objectIdentity={}", identity);

    aclService.createAcl(identity);
  }

  @Transactional
  public void deleteAccessList(AccessListRequest request) {
    ObjectIdentity identity = getIdentity(request.getDomainObject());

    log.info("Deleting access list: objectIdentity={}", identity);

    aclService.deleteAcl(identity, true);
  }

  @Transactional
  public void grantAccess(UserPermissionRequest request) {
    MutableAcl acl = getAcl(request.getDomainObject());
    Sid sid = new UserAuthoritySid(request.getUsedId(), request.getAuthority());
    Set<Permission> permissions = convertPermissions(request.getPermissions());

    validateGrantAccess(acl, sid, permissions);
    grantAccess(acl, sid, permissions);

    aclService.updateAcl(acl);
  }

  @Transactional
  public void revokeAccess(UserPermissionRequest request) {
    MutableAcl acl = getAcl(request.getDomainObject());
    Sid sid = new UserAuthoritySid(request.getUsedId(), request.getAuthority());
    Set<Permission> permissions = convertPermissions(request.getPermissions());

    revokeAccess(acl, sid, permissions);

    aclService.updateAcl(acl);
  }

  private ObjectIdentity getIdentity(Object domainObject) {
    return objectIdentityRetrievalStrategy.getObjectIdentity(domainObject);
  }

  private MutableAcl getAcl(Object domainObject) {
    ObjectIdentity identity = getIdentity(domainObject);

    try {
      return (MutableAcl) aclService.readAclById(identity);
    } catch (NotFoundException exception) {
      throw new AccessListNotFoundException(identity, exception);
    }
  }

  private Set<Permission> convertPermissions(Set<SensPermission> permissions) {
    return permissions
        .stream()
        .map(SensPermission::name)
        .map(permissionFactory::buildFromName)
        .collect(toSet());
  }

  private static void validateGrantAccess(MutableAcl acl, Sid sid, Set<Permission> permissions) {
    permissions.forEach(p -> validateGrantAccess(acl, sid, p));
  }

  private static void validateGrantAccess(MutableAcl acl, Sid sid, Permission permission) {
    if (acl.getEntries().stream().anyMatch(e -> hasAccessForSid(e, sid, permission)))
      throw new UserPermissionAlreadyGrantedException(sid, permission);
  }

  private static void grantAccess(MutableAcl acl, Sid sid, Set<Permission> permissions) {
    permissions.forEach(p -> acl.insertAce(acl.getEntries().size(), p, sid, true));
  }

  private static void revokeAccess(MutableAcl acl, Sid sid, Set<Permission> permissions) {
    permissions.forEach(p -> revokeAccess(acl, sid, p));
  }

  private static void revokeAccess(MutableAcl acl, Sid sid, Permission permission) {
    List<AccessControlEntry> accesses = acl.getEntries();
    IntStream
        .range(0, accesses.size())
        .filter(i -> hasAccessForSid(accesses.get(i), sid, permission))
        .findFirst()
        .ifPresent(acl::deleteAce);
  }

  private static boolean hasAccessForSid(
      AccessControlEntry access, Sid sid, Permission permission) {

    return access.isGranting()
        && access.getSid().equals(sid)
        && access.getPermission().equals(permission);
  }
}
