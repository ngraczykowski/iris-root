package com.silenteight.sens.webapp.security.acl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public class AccessListFinder {

  @NonNull
  private final ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy;

  @NonNull
  private final LookupStrategy lookupStrategy;

  @Transactional(readOnly = true)
  public Collection<Object> findWithoutAccessList(Collection<Object> domainObjects) {
    Map<ObjectIdentity, Object> objectIdentities = domainObjects
        .stream()
        .collect(toMap(objectIdentityRetrievalStrategy::getObjectIdentity, identity()));

    Set<ObjectIdentity> identities = objectIdentities.keySet();
    Set<ObjectIdentity> identitiesWithAcl = lookupStrategy
        .readAclsById(new ArrayList<>(identities), null)
        .keySet();

    identities.removeAll(identitiesWithAcl);

    return objectIdentities.values();
  }
}
