package com.silenteight.sens.webapp.security.acl;

import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;

abstract class AbstractDomainObjectIdentityGenerator implements ObjectIdentityRetrievalStrategy {

  abstract String getType();

  abstract String mapIdentifier(Object domainObject);

  public final ObjectIdentity getObjectIdentity(Object domainObject) {
    return new ObjectIdentityImpl(getType(), mapIdentifier(domainObject));
  }
}
