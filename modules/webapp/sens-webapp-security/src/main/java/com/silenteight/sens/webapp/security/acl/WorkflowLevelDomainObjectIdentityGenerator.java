package com.silenteight.sens.webapp.security.acl;

import com.silenteight.sens.webapp.kernel.domain.WorkflowLevel;

class WorkflowLevelDomainObjectIdentityGenerator extends AbstractDomainObjectIdentityGenerator {

  private static final String TYPE_NAME = WorkflowLevel.class.getName();

  @Override
  public String getType() {
    return TYPE_NAME;
  }

  @Override
  String mapIdentifier(Object domainObject) {
    WorkflowLevel workflowLevel = (WorkflowLevel) domainObject;

    return workflowLevel.getDecisionTreeId() + "@" + workflowLevel.getLevel();
  }
}
