package com.silenteight.sens.webapp.security.acl;

import org.springframework.security.acls.domain.ObjectIdentityRetrievalStrategyImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CompositeObjectIdentityRetrievalStrategy implements ObjectIdentityRetrievalStrategy {

  // XXX(ahaczewski): Probably (PROBABLY!) using default strategy when no specific generator is
  // found might not (MIGHT NOT!) be the best idea. Rethink it sometime down the road.
  private static final ObjectIdentityRetrievalStrategy DEFAULT_STRATEGY =
      new ObjectIdentityRetrievalStrategyImpl();

  private final Map<String, ObjectIdentityRetrievalStrategy> strategies = new HashMap<>();

  CompositeObjectIdentityRetrievalStrategy(List<AbstractDomainObjectIdentityGenerator> strategies) {
    strategies.forEach(this::addGenerator);
  }

  private void addGenerator(AbstractDomainObjectIdentityGenerator generator) {
    strategies.put(generator.getType(), generator);
  }

  @Override
  public ObjectIdentity getObjectIdentity(Object domainObject) {
    String domainType = domainObject.getClass().getName();

    ObjectIdentityRetrievalStrategy generator =
        strategies.getOrDefault(domainType, DEFAULT_STRATEGY);

    return generator.getObjectIdentity(domainObject);
  }
}
