package com.silenteight.sens.webapp.security.acl;

import com.silenteight.sens.webapp.kernel.domain.DecisionTreeId;

class DecisionTreeIdDomainObjectIdentityGenerator extends AbstractDomainObjectIdentityGenerator {

  private static final String TYPE_NAME = DecisionTreeId.class.getName();

  @Override
  public String getType() {
    return TYPE_NAME;
  }

  @Override
  String mapIdentifier(Object domainObject) {
    DecisionTreeId decisionTreeId = (DecisionTreeId) domainObject;

    return String.valueOf(decisionTreeId.getValue());
  }
}
